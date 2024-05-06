package ru.pin120.carwashemployee.CategoriesAndServices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

public class EditServiceController implements Initializable {

    @FXML
    private Button btCancel;

    @FXML
    private TextField serviceNameField;
    @FXML
    private Stage stage;
    private FXOperationMode mode;
    private ResourceBundle rb;
    private ServiceDTO serviceDTO;

    @Getter
    private FXFormExitMode exitMode;

    private ServiceRepository serviceRepository = new ServiceRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForTextField(serviceNameField);
        serviceNameFieldTextListener();
    }

    private void serviceNameFieldTextListener(){
        serviceNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ServiceFX.MAX_LENGTH_SERVICE_NAME) {
                    serviceNameField.setText(oldValue);
                }
            }
        });
    }

    public void setParameters(ServiceDTO serviceDTO, FXOperationMode mode, Stage stage){
        this.serviceDTO = serviceDTO;
        this.mode = mode;
        this.stage = stage;
        switch (mode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_FORM_TITLE") + " " + serviceDTO.getCatName());
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_FORM_TITLE"));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_FORM_TITLE"));
                serviceNameField.setDisable(true);
                break;

        }
        serviceNameField.setText(serviceDTO.getServName());

        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    @FXML
    private void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(serviceNameField.getText() == null || serviceNameField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("EMPTY_SERVICE_NAME"));
            serviceNameField.clear();
            serviceNameField.requestFocus();
        }else{
            try{
                switch (mode){
                    case CREATE:
                        serviceDTO.setServName(serviceNameField.getText().trim());
                        canExit = serviceRepository.createService(serviceDTO);
                        break;
                    case DELETE:
                        canExit = serviceRepository.deleteService(serviceDTO);
                        break;
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }else{
            if(mode == FXOperationMode.CREATE || mode == FXOperationMode.EDIT){
                serviceNameField.requestFocus();
            }else{
                btCancel.requestFocus();
            }
        }

    }
    @FXML
    private void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction(){
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
