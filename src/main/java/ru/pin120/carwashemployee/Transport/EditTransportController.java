package ru.pin120.carwashemployee.Transport;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 *  онтроллер редактировани€ данных о транспорте
 */
public class EditTransportController implements Initializable {

    @FXML
    private Button getAvailableCategoriesButton;
    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private ComboBox<CategoryOfTransport> transportCategoryComboBox;
    @FXML
    private TextField modelField;
    @FXML
    private TextField markField;
    private ResourceBundle rb;
    @FXML
    private Stage stage;

    @Getter
    private FXFormExitMode exitMode;

    private FXOperationMode operationMode;
    private Transport transport;

    private TransportRepository transportRepository = new TransportRepository();
    private CategoryOfTransportRepository categoryOfTransportRepository = new CategoryOfTransportRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForEditableTextField(modelField);
        FXHelper.setContextMenuForEditableTextField(markField);

        markFieldTextListener();
        modelFieldTextListener();

        Platform.runLater(this::convertCategoryOfTransportToString);
    }


    private void markFieldTextListener() {
        markField.textProperty().addListener((observable, oldValue, newValue) -> {
            transportCategoryComboBox.getItems().clear();
            btOK.setDisable(false);
            if(newValue != null) {
                if (newValue.length() > TransportFX.MAX_LENGTH_MARK) {
                    markField.setText(oldValue);
                }
            }
        });
    }

    private void modelFieldTextListener() {
        modelField.textProperty().addListener((observable, oldValue, newValue) -> {
            transportCategoryComboBox.getItems().clear();
            btOK.setDisable(false);
            if(newValue != null) {
                if (newValue.length() > TransportFX.MAX_LENGTH_MODEL) {
                    modelField.setText(oldValue);
                }
            }
        });
    }


    public void setParameters(Transport transport, FXOperationMode operationMode, Stage modalStage){
        this.transport = transport;
        this.operationMode = operationMode;
        this.stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                Platform.runLater(this::fillingComponents);
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                Platform.runLater(this::fillingComponents);
                markField.setEditable(false);
                modelField.setEditable(false);
                transportCategoryComboBox.setDisable(true);
                getAvailableCategoriesButton.setDisable(true);
                Platform.runLater(()->btOK.requestFocus());
                break;
        }


        this.stage.setMaxHeight(170);
        closeWindowAction();
    }

    private void fillingComponents(){
        markField.setText(transport.getTrMark());
        modelField.setText(transport.getTrModel());

        try{
            List<CategoryOfTransport> categoryOfTransports = categoryOfTransportRepository.getAvailableCategoriesForTransport(transport.getTrMark(), transport.getTrModel(), transport.getTrId());
            System.out.println(transport);
            transportCategoryComboBox.getItems().setAll(categoryOfTransports);

            Optional<CategoryOfTransport> categoryOfTransportOptional = categoryOfTransports.stream()
                            .filter(t->t.getCatTrName().equals(transport.getCategoryOfTransport().getCatTrName()))
                            .findFirst();

            categoryOfTransportOptional.ifPresent(c->transportCategoryComboBox.getSelectionModel().select(c));

        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;

        if(markField.getText() == null || markField.getText().isBlank()){
            markField.clear();
            FXHelper.showErrorAlert(rb.getString("MARK_NOT_EMPTY"));
            markField.requestFocus();;
        }else if(modelField.getText() == null || modelField.getText().isBlank()){
            modelField.clear();
            FXHelper.showErrorAlert(rb.getString("MODEL_NOT_EMPTY"));
            modelField.requestFocus();
        }else if(transportCategoryComboBox.getSelectionModel().getSelectedItem() == null){
            FXHelper.showErrorAlert(rb.getString("CATEGORY_NOT_EMPTY"));
            getAvailableCategoriesButton.requestFocus();
        }else if(!markField.getText().matches(TransportFX.MARK_REGEX)){
            FXHelper.showErrorAlert(rb.getString("MARK_VALID_CHARACTERS"));
            markField.requestFocus();
        }else if(!modelField.getText().matches(TransportFX.MODEL_REGEX)){
            FXHelper.showErrorAlert(rb.getString("MODEL_VALID_CHARACTERS"));
            modelField.requestFocus();
        } else{
            try {
                switch (operationMode) {
                    case CREATE:
                        transport.setTrMark(markField.getText().trim());
                        transport.setTrModel(modelField.getText().trim());
                        transport.setCategoryOfTransport(transportCategoryComboBox.getSelectionModel().getSelectedItem());

                        Transport createdTransport = transportRepository.create(transport);
                        if(createdTransport != null){
                            canExit = true;
                            transport.setTrId(createdTransport.getTrId());
                        }
                        break;
                    case EDIT:
                        if(transport.getTrMark().equals(markField.getText().trim()) && transport.getTrModel().equals(modelField.getText().trim()) &&
                        transport.getCategoryOfTransport().getCatTrName().equals(transportCategoryComboBox.getSelectionModel().getSelectedItem().getCatTrName())){
                            canExit = true;
                        }else {
                            transport.setTrMark(markField.getText().trim());
                            transport.setTrModel(modelField.getText().trim());
                            transport.setCategoryOfTransport(transportCategoryComboBox.getSelectionModel().getSelectedItem());
                            if (transportRepository.edit(transport) != null) {
                                canExit = true;
                            }
                        }
                        break;
                    case DELETE:
                            canExit = transportRepository.delete(transport.getTrId());
                        break;
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit) {
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
//        }else{
//            if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT){
//                markField.requestFocus();
//            }else{
//                btCancel.requestFocus();
//            }
//        }
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    public void getAvailableCategoriesButtonAction(ActionEvent actionEvent) {
        try{
            String mark = markField.getText().trim();
            String model = modelField.getText().trim();

            if(!mark.isBlank() && !model.isBlank()) {
                List<CategoryOfTransport> categoryOfTransports = operationMode == FXOperationMode.CREATE ? categoryOfTransportRepository.getAvailableCategoriesForTransport(mark, model,null) : categoryOfTransportRepository.getAvailableCategoriesForTransport(mark, model, transport.getTrId());
                transportCategoryComboBox.getItems().setAll(categoryOfTransports);

                transportCategoryComboBox.getSelectionModel().selectFirst();
                if (transportCategoryComboBox.getItems().isEmpty()) {
                    btOK.setDisable(true);
                }
            }else{
                FXHelper.showErrorAlert(rb.getString("MARK_AND_MODEL_NOT_EMPTY"));
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            btCancel.requestFocus();
        }
    }

    private void convertCategoryOfTransportToString(){
        try {
            transportCategoryComboBox.setConverter(new StringConverter<CategoryOfTransport>() {
                @Override
                public String toString(CategoryOfTransport object) {
                    if(object == null){
                        return "";
                    }
                    return object.getCatTrName();
                }

                @Override
                public CategoryOfTransport fromString(String string) {
                    return null;
                }
            });
        }catch (Exception e){}
    }
}
