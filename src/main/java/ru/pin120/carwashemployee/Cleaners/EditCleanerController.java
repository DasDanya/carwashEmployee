package ru.pin120.carwashemployee.Cleaners;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Clients.ClientFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditCleanerController implements Initializable {
    @FXML
    private Button btCancel;
    @FXML
    private Button btOK;
    @FXML
    private Button showImageButton;
    @FXML
    private Button loadImageButton;
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField patronymicField;
    @FXML
    private TextField phoneField;
    @FXML
    private TextField photoInfoField;
    @FXML
    private ComboBox<CleanerStatus> statusComboBox;
    private ResourceBundle rb;
    @FXML
    private Stage stage;
    private Cleaner cleaner;
    private FXOperationMode operationMode;

    @Getter
    private FXFormExitMode exitMode;
    private File selectedPhoto;

    private BoxesRepository boxesRepository = new BoxesRepository();
    private CleanersRepository cleanersRepository = new CleanersRepository();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        FXHelper.setContextMenuForEditableTextField(surnameField);
        FXHelper.setContextMenuForEditableTextField(nameField);
        FXHelper.setContextMenuForEditableTextField(patronymicField);
        FXHelper.setContextMenuForEditableTextField(phoneField);
        FXHelper.setContextMenuForNotEditableTextField(photoInfoField);

        settingTooltipForButtons();
        setConvertersForComboBoxes();
        textFieldListeners();
    }

    private void textFieldListeners(){
        surnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > CleanerFX.MAX_SURNAME_LENGTH) {
                    surnameField.setText(oldValue);
                }
            }
        });
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > CleanerFX.MAX_NAME_LENGTH) {
                    nameField.setText(oldValue);
                }
            }
        });
        patronymicField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > CleanerFX.MAX_PATRONYMIC_LENGTH) {
                    patronymicField.setText(oldValue);
                }
            }
        });
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String digitsOnly = newValue.replaceAll("\\D", "");

                if (digitsOnly.length() > CleanerFX.MAX_PHONE_FILLING) {
                    digitsOnly = digitsOnly.substring(0, ClientFX.MAX_PHONE_FILLING);
                }

                phoneField.setText(digitsOnly);
            }
        });
    }


    private void fillingComboBoxes() {
        statusComboBox.getItems().setAll(CleanerStatus.values());
    }

    private void setConvertersForComboBoxes() {
        statusComboBox.setConverter(new StringConverter<CleanerStatus>() {
            @Override
            public String toString(CleanerStatus status) {
                return status == null ? null : status.getDisplayValue();
            }

            @Override
            public CleanerStatus fromString(String string) {
                return null;
            }
        });
    }

    private void settingTooltipForButtons() {
        loadImageButton.setOnMouseEntered(event -> {
            loadImageButton.setTooltip(new Tooltip(rb.getString("LOAD_PHOTO")));
        });
        showImageButton.setOnMouseEntered(event -> {
            showImageButton.setTooltip(new Tooltip(rb.getString("SHOW_PHOTO")));
        });
    }

    public void setParameters(Cleaner cleaner, FXOperationMode operationMode, Stage stage){
        this.cleaner = cleaner;
        this.operationMode = operationMode;
        this.stage = stage;

        fillingComboBoxes();
        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                statusComboBox.getSelectionModel().select(CleanerStatus.ACT);
                photoInfoField.setText(rb.getString("DEFAULT_PHOTO"));
                statusComboBox.setDisable(true);
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                photoInfoField.setText(rb.getString("CLEANER_PHOTO"));
                fillingComponents();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                photoInfoField.setText(rb.getString("CLEANER_PHOTO"));
                fillingComponents();
                surnameField.setEditable(false);
                nameField.setEditable(false);
                patronymicField.setEditable(false);
                phoneField.setEditable(false);
                statusComboBox.setDisable(true);
                loadImageButton.setDisable(true);

                Platform.runLater(()->btOK.requestFocus());
                break;
        }

        this.stage.setMaxHeight(330);
        closeWindowAction();
    }

    private void fillingComponents(){
        surnameField.setText(cleaner.getClrSurname());
        nameField.setText(cleaner.getClrName());
        patronymicField.setText(cleaner.getClrPatronymic());
        phoneField.setText(cleaner.getClrPhone().substring(2));
        statusComboBox.getSelectionModel().select(cleaner.getClrStatus());
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(surnameField.getText() == null || surnameField.getText().isBlank()){
            surnameField.clear();
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_SURNAME"));
            surnameField.requestFocus();
        }else if(nameField.getText() == null || nameField.getText().isBlank()){
            nameField.clear();
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_NAME"));
            nameField.requestFocus();
        }else if(phoneField.getText() == null || phoneField.getText().isBlank() || phoneField.getText().trim().length() < CleanerFX.MAX_PHONE_FILLING){
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_PHONE"));
            phoneField.requestFocus();
        }else if(!surnameField.getText().matches(CleanerFX.SURNAME_REGEX)){
            FXHelper.showErrorAlert(rb.getString("SURNAME_VALID_CHARACTERS"));
            surnameField.requestFocus();
        }else if(!nameField.getText().matches(CleanerFX.NAME_REGEX)) {
            FXHelper.showErrorAlert(rb.getString("NAME_VALID_CHARACTERS"));
            nameField.requestFocus();
        }else if(patronymicField.getText() != null && !patronymicField.getText().matches(CleanerFX.PATRONYMIC_REGEX)){
                FXHelper.showErrorAlert(rb.getString("PATRONYMIC_VALID_CHARACTERS"));
                patronymicField.requestFocus();
        }else{
            try{
                switch (operationMode){
                    case CREATE:
                        cleaner.setClrSurname(surnameField.getText().trim());
                        cleaner.setClrName(nameField.getText().trim());
                        cleaner.setClrPatronymic(patronymicField.getText().trim());
                        cleaner.setClrPhone("+7"+phoneField.getText().trim());
                        cleaner.setClrStatus(statusComboBox.getValue());

                        Cleaner createdCleaner = cleanersRepository.create(cleaner, selectedPhoto);
                        if(createdCleaner != null){
                            canExit = true;
                            cleaner.setClrId(createdCleaner.getClrId());
                            cleaner.setClrPhotoName(createdCleaner.getClrPhotoName());
                        }
                        break;
                    case EDIT:
                        if(cleaner.getClrSurname().equals(surnameField.getText().trim()) && cleaner.getClrName().equals(nameField.getText().trim()) && ((cleaner.getClrPatronymic() == null && patronymicField.getText() == null) || cleaner.getClrPatronymic().equals(patronymicField.getText().trim())) &&
                        cleaner.getClrPhone().equals("+7"+phoneField.getText().trim()) && cleaner.getClrStatus() == statusComboBox.getValue()  && photoInfoField.getText().equals(rb.getString("CLEANER_PHOTO"))){
                            canExit = true;
                        }else{
                            cleaner.setClrSurname(surnameField.getText().trim());
                            cleaner.setClrName(nameField.getText().trim());
                            cleaner.setClrPatronymic(patronymicField.getText() != null ? patronymicField.getText().trim() : null);
                            cleaner.setClrPhone("+7"+phoneField.getText().trim());
                            cleaner.setClrStatus(statusComboBox.getValue());

                            Cleaner editedCleaner = cleanersRepository.edit(cleaner, selectedPhoto);
                            if(editedCleaner != null){
                                canExit = true;
                                cleaner.setClrPhotoName(editedCleaner.getClrPhotoName());
                            }
                        }
                        break;
                    case DELETE:
                        canExit = cleanersRepository.delete(cleaner.getClrId());
                        break;
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
            if(canExit){
                exitMode = FXFormExitMode.OK;
                stage.close();
            }else{
                if(operationMode == FXOperationMode.DELETE){
                    btCancel.requestFocus();
                }
            }
        }
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    public void loadImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("SELECT_PHOTO"));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("IMAGES", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );

        selectedPhoto = fileChooser.showOpenDialog(stage);
        if(selectedPhoto != null){
            photoInfoField.setText(rb.getString("NEW_PHOTO") + " " + selectedPhoto.getAbsolutePath());
        }
    }

    private Scene getActualScene(){
        return surnameField.getScene();
    }

    public void showImageButtonAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Cleaners.resources.ShowCleanerPhoto", "Cleaners/fxml/ShowCleanerPhoto.fxml", getActualScene());
            ShowCleanerPhotoController showCleanerPhotoController = fxWindowData.getLoader().getController();
            if(operationMode == FXOperationMode.CREATE || ((operationMode == FXOperationMode.EDIT || operationMode == FXOperationMode.DELETE) && selectedPhoto != null)) {
                if(operationMode == FXOperationMode.CREATE) {
                    showCleanerPhotoController.showPhoto(FXHelper.convertToImage(selectedPhoto));
                }else{
                    showCleanerPhotoController.showPhoto(FXHelper.convertToImage(selectedPhoto), cleaner);
                }
            }else{
                showCleanerPhotoController.showPhoto(cleaner);
            }
            fxWindowData.getModalStage().showAndWait();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }
}
