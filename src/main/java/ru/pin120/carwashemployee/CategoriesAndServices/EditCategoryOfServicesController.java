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
 * Контроллер редактирования данных о категории услуг
 */
public class EditCategoryOfServicesController implements Initializable {

    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private TextField categoryNameField;
    @FXML
    private Stage stage;
    private FXOperationMode mode;
    private ResourceBundle rb;
    private CategoryOfServices categoryOfServices;

    @Getter
    private FXFormExitMode exitMode;

    private CategoriesOfServicesRepository categoriesOfServicesRepository = new CategoriesOfServicesRepository();

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForEditableTextField(categoryNameField);
        categoryNameFieldTextListener();
    }

    /**
     * Слушатель для поля ввода названия категории, ограничивает длину вводимого текста.
     */
    private void categoryNameFieldTextListener(){
        categoryNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > CategoryOfServicesFX.MAX_LENGTH_CATEGORY_NAME) {
                    categoryNameField.setText(oldValue);
                }
            }
        });
    }

    /**
     * Устанавливает параметры для формы редактирования категории
     *
     * @param categoryOfServices объект категории услуг
     * @param mode режим операции (создание, удаление)
     * @param stage (Stage) для отображения формы
     */
    public void setParameters(CategoryOfServices categoryOfServices, FXOperationMode mode, Stage stage){
        this.categoryOfServices = categoryOfServices;
        this.mode = mode;
        this.stage = stage;
        switch (mode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_FORM_TITLE"));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_FORM_TITLE"));
                categoryNameField.setEditable(false);
                Platform.runLater(()->btOK.requestFocus());
                break;

        }
        categoryNameField.setText(categoryOfServices.getCatName());

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
        if(categoryNameField.getText() == null || categoryNameField.getText().isBlank()) {
            FXHelper.showErrorAlert(rb.getString("EMPTY_CATEGORY_NAME"));
            categoryNameField.clear();
            categoryNameField.requestFocus();
        }else if(!categoryNameField.getText().matches(CategoryOfServicesFX.REGEX)){
            FXHelper.showErrorAlert(rb.getString("CATEGORY_NAME_VALID_CHARACTERS"));
            categoryNameField.requestFocus();
        }else{
            try {
                switch (mode) {
                    case CREATE:
                        categoryOfServices.setCatName(categoryNameField.getText().trim());
                        canExit = categoriesOfServicesRepository.createCategoryOfServices(categoryOfServices);
                        break;
//                    case EDIT:
//                        EditCategoryOrServiceDTO editCategoryOrServiceDTO = new EditCategoryOrServiceDTO(categoryOfServices.getCatName(), categoryNameField.getText().trim());
//                        canExit = categoriesOfServicesRepository.editCategoryOfServices(editCategoryOrServiceDTO);
//                        categoryOfServices.setCatName(categoryNameField.getText().trim());
//                        break;
                    case DELETE:
                        canExit = categoriesOfServicesRepository.deleteCategoryOfServices(categoryOfServices);
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
                categoryNameField.requestFocus();
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
