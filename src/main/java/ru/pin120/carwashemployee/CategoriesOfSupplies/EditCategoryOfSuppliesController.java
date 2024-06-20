package ru.pin120.carwashemployee.CategoriesOfSupplies;

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
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportFX;
import ru.pin120.carwashemployee.Cleaners.CleanerStatus;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования данных о категории расходных материалов
 *
 */
public class EditCategoryOfSuppliesController implements Initializable {

    private static final int MAX_LENGTH_CATEGORY_NAME = 50;
    private static final String CATEGORY_REGEX = "^[а-яА-ЯёЁ -]+$";

    @FXML
    private ComboBox<UnitOfMeasure> unitComboBox;
    @FXML
    private Button btCancel;
    @FXML
    private Button btOK;
    @FXML
    private TextField categoryNameField;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    private CategoryOfSupplies categoryOfSupplies;
    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();

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
        setConvertersForComboBoxes();
    }

    /**
     * Устанавливает значения и конвертеры для выпадающего списка единиц измерения.
     * Заполняет элементы выпадающего списка единицами измерения {@link UnitOfMeasure}.
     * Устанавливает конвертер для правильного отображения выбранных значений в строковом формате.
     */
    private void setConvertersForComboBoxes() {
        unitComboBox.getItems().setAll(UnitOfMeasure.values());
        unitComboBox.setConverter(new StringConverter<UnitOfMeasure>() {
            @Override
            public String toString(UnitOfMeasure unit) {
                return unit.getDisplayValue();
            }

            @Override
            public UnitOfMeasure fromString(String string) {
                return null;
            }
        });
    }


    /**
     * Устанавливает слушатель изменений текста для поля названия категории.
     * Проверяет длину введенного значения и обрезает его до максимально допустимой длины, если необходимо.
     */
    private void categoryNameFieldTextListener() {
        categoryNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > MAX_LENGTH_CATEGORY_NAME) {
                    categoryNameField.setText(oldValue);
                }
            }
        });
    }

    /**
     * Устанавливает параметры для формы создания или удаления категории расходных материалов.
     *
     * @param categoryOfSupplies Объект {@link CategoryOfSupplies}, содержащий данные о категории расходных материалов.
     * @param operationMode Режим операции (создание или удаление).
     * @param modalStage Модальное окно, на котором отображается форма.
     */
    public void setParameters(CategoryOfSupplies categoryOfSupplies, FXOperationMode operationMode, Stage modalStage) {
        this.categoryOfSupplies = categoryOfSupplies;
        this.operationMode = operationMode;
        stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_FORM_TITLE"));
                unitComboBox.getSelectionModel().selectFirst();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_FORM_TITLE"));
                unitComboBox.getSelectionModel().select(categoryOfSupplies.getUnit());
                categoryNameField.setEditable(false);
                unitComboBox.setDisable(true);
                Platform.runLater(()->btOK.requestFocus());
                break;

        }

        categoryNameField.setText(categoryOfSupplies.getCsupName());
        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    /**
     * Обработчик события нажатия кнопки "OK".
     *
     * @param actionEvent Событие действия, инициированное нажатием кнопки "OK".
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(categoryNameField.getText() == null || categoryNameField.getText().isBlank()) {
            FXHelper.showErrorAlert(rb.getString("EMPTY_CATEGORY_NAME"));
            categoryNameField.clear();
            categoryNameField.requestFocus();
        }else if(!categoryNameField.getText().matches(CATEGORY_REGEX)){
            FXHelper.showErrorAlert(rb.getString("CATEGORY_NAME_VALID_CHARACTERS"));
            categoryNameField.requestFocus();
        }else{
            try{
                if(operationMode == FXOperationMode.CREATE){
                    categoryOfSupplies.setCsupName(categoryNameField.getText().trim());
                    categoryOfSupplies.setUnit(unitComboBox.getSelectionModel().getSelectedItem());
                    if(categoryOfSuppliesRepository.create(categoryOfSupplies) != null){
                        canExit = true;
                    }
                }else{
                    canExit = categoryOfSuppliesRepository.delete(categoryOfSupplies.getCsupName());
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }else{
            if(operationMode == FXOperationMode.CREATE){
                categoryNameField.requestFocus();
            }else{
                btCancel.requestFocus();
            }
        }
    }

    /**
     * Обработчик события нажатия кнопки "Отмена".
     * Устанавливает режим завершения формы на CANCEL и закрывает модальное окно.
     *
     * @param actionEvent Событие действия, инициированное нажатием кнопки "Отмена".
     */
    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    /**
     * Устанавливает действие на событие закрытия окна.
     * Устанавливает режим завершения формы на "Выход" при закрытии окна пользователем.
     */
    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

}
