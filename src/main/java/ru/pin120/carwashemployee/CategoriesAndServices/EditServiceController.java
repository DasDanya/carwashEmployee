package ru.pin120.carwashemployee.CategoriesAndServices;

import javafx.application.Platform;
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

/**
 * Контроллер редактирования данных об услуге
 */
public class EditServiceController implements Initializable {

    @FXML
    private Button btOK;
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

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForEditableTextField(serviceNameField);
        serviceNameFieldTextListener();
    }

    /**
     * Слушатель для поля ввода названия услуги, ограничивает длину вводимого текста.
     */
    private void serviceNameFieldTextListener(){
        serviceNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ServiceFX.MAX_LENGTH_SERVICE_NAME) {
                    serviceNameField.setText(oldValue);
                }
            }
        });
    }

    /**
     * Устанавливает параметры для формы редактирования услуги
     *
     * @param serviceDTO объект услуги
     * @param mode режим операции (создание, удаление)
     * @param stage (Stage) для отображения формы
     */
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
                serviceNameField.setEditable(false);
                Platform.runLater(()->btOK.requestFocus());
                break;

        }
        serviceNameField.setText(serviceDTO.getServName());

        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    /**
     * Обрабатывает нажатие кнопки OK, выполняет операцию в зависимости от режима (создание, удаление).
     *
     * @param actionEvent событие нажатия кнопки
     */
    @FXML
    private void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(serviceNameField.getText() == null || serviceNameField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("EMPTY_SERVICE_NAME"));
            serviceNameField.clear();
            serviceNameField.requestFocus();
        } else if(!serviceNameField.getText().matches(ServiceFX.REGEX)){
            FXHelper.showErrorAlert(rb.getString("SERVICE_NAME_VALID_CHARACTERS"));
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

    /**
     * Обрабатывает нажатие кнопки Cancel, закрывает форму.
     *
     * @param actionEvent событие нажатия кнопки
     */
    @FXML
    private void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    /**
     * Устанавливает действие при закрытии окна, чтобы задать режим выхода.
     */
    private void closeWindowAction(){
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
