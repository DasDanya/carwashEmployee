package ru.pin120.carwashemployee.PriceListPosition;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import java.time.Duration;

import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования данных о позиции прайс-листа
 */
public class EditPriceListPositionController implements Initializable {


    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private Spinner<Integer> priceSpinner;
    @FXML
    private Spinner<Integer> timeSpinner;
    @FXML
    private ComboBox<CategoryOfTransport> transportCategoryComboBox;
    private ResourceBundle rb;
    @FXML
    private Stage stage;

    private PriceListPosition priceListPosition;

    private FXOperationMode operationMode;

    @Getter
    private FXFormExitMode exitMode;

    private CategoryOfTransportRepository categoryOfTransportRepository = new CategoryOfTransportRepository();
    private PriceListPositionRepository priceListPositionRepository = new PriceListPositionRepository();

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        rb = resourceBundle;
        try {
            priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, PriceListPositionFX.MAX_PRICE, 0, 50));
            timeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(AppHelper.getStepServiceTime(), Math.abs((int)Duration.between(AppHelper.getStartWorkTime(), AppHelper.getEndWorkTime()).toMinutes()), AppHelper.getStepServiceTime(), AppHelper.getStepServiceTime()));
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

        FXHelper.setContextMenuForEditableTextField(priceSpinner.getEditor());
        FXHelper.setContextMenuForEditableTextField(timeSpinner.getEditor());

        try {
            setSpinnersFormatters();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

    }


    /**
     * Устанавливает форматеры для спиннеров.
     * Форматер для спиннера стоимости позволяет вводить только цифры и ограничивает значение максимальной стоимости.
     * Форматер для спиннера времени позволяет вводить только цифры и ограничивает значение максимального времени работы.
     */
    private void setSpinnersFormatters(){
        TextFormatter<Integer> priceFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= PriceListPositionFX.MAX_PRICE) {
                    return change;
                }
            }
            return null;
        });

        priceSpinner.getEditor().setTextFormatter(priceFormatter);

        TextFormatter<Integer> timeFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= Math.abs((int)Duration.between(AppHelper.getStartWorkTime(), AppHelper.getEndWorkTime()).toMinutes()) || newValue >= AppHelper.getStepServiceTime()) {
                    return change;
                }
            }
            return null;
        });

        timeSpinner.getEditor().setTextFormatter(timeFormatter);
    }

    /**
     * Устанавливает параметры для редактирования позиции прайс-листа.
     *
     * @param priceListPosition позиция прайс-листа, которую нужно редактировать.
     * @param operationMode режим операции (создание, редактирование, удаление).
     * @param stage Stage.
     */
    public void setParameters(PriceListPosition priceListPosition, FXOperationMode operationMode, Stage stage){
        this.priceListPosition = priceListPosition;
        this.operationMode = operationMode;
        this.stage = stage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                fillingTransportCategory();
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                fillingComponents();
                transportCategoryComboBox.setDisable(true);
                Platform.runLater(()->priceSpinner.getEditor().positionCaret(priceSpinner.getEditor().getText().length()));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                fillingComponents();
                transportCategoryComboBox.setDisable(true);
                priceSpinner.setDisable(true);
                timeSpinner.setDisable(true);
                break;
        }

        this.stage.setMaxHeight(170);
        closeWindowAction();

        Platform.runLater(this::convertCategoryOfTransportToString);

    }

    /**
     * Заполняет компоненты данными текущей позиции прайс-листа.
     */
    private void fillingComponents(){
        priceSpinner.getValueFactory().setValue(priceListPosition.getPlPrice());
        timeSpinner.getValueFactory().setValue(priceListPosition.getPlTime());
        transportCategoryComboBox.getItems().add(priceListPosition.getCategoryOfTransport());
    }

    /**
     * Конвертирует объекты CategoryOfTransport в строки для отображения в ComboBox.
     */
    private void convertCategoryOfTransportToString(){
        transportCategoryComboBox.setConverter(new StringConverter<CategoryOfTransport>() {
            @Override
            public String toString(CategoryOfTransport object) {
                return object.getCatTrName();
            }
            @Override
            public CategoryOfTransport fromString(String string) {
                return null;
            }
        });

        transportCategoryComboBox.getSelectionModel().selectFirst();
        if(transportCategoryComboBox.getItems().isEmpty()){
            btOK.setDisable(true);
            btCancel.requestFocus();
        }
    }


    /**
     * Заполняет ComboBox категориями транспорта, для которых не установлена стоимость и время.
     */
    private void fillingTransportCategory(){
        try{
            List<CategoryOfTransport> categoriesWithoutPriceAndTime = categoryOfTransportRepository.getCategoriesOfTransportWithoutPriceAndTime(priceListPosition.getService().getServName());
            transportCategoryComboBox.getItems().setAll(categoriesWithoutPriceAndTime);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * Устанавливает действие на событие закрытия окна.
     * Устанавливает режим завершения формы на "Выход" при закрытии окна пользователем.
     */
    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }


    /**
     * Обработчик события нажатия кнопки "OK".
     *
     * @param actionEvent Событие действия, инициированное нажатием кнопки "OK".
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(priceSpinner.getValue() == 0){
            FXHelper.showErrorAlert(rb.getString("PRICE_NOT_ZERO"));
        }else{
            try {
                switch (operationMode) {
                    case CREATE:
                        priceListPosition.setPlPrice(priceSpinner.getValue());
                        priceListPosition.setPlTime(timeSpinner.getValue());
                        priceListPosition.setCategoryOfTransport(transportCategoryComboBox.getSelectionModel().getSelectedItem());

                        PriceListPosition createdPriceListPosition = priceListPositionRepository.createPriceListPosition(priceListPosition);
                        if(createdPriceListPosition != null){
                            canExit = true;

                            priceListPosition.setPlId(createdPriceListPosition.getPlId());
                            priceListPosition.setService(createdPriceListPosition.getService());
                            priceListPosition.setCategoryOfTransport(createdPriceListPosition.getCategoryOfTransport());
                            priceListPosition.setPlPrice(priceListPosition.getPlPrice());
                            priceListPosition.setPlTime(priceListPosition.getPlTime());
                        }
                        break;
                    case EDIT:
                        if(priceListPosition.getPlPrice().intValue() == priceSpinner.getValue().intValue() && priceListPosition.getPlTime().intValue() == timeSpinner.getValue().intValue()){
                            canExit = true;
                        }else{
                            priceListPosition.setPlPrice(priceSpinner.getValue());
                            priceListPosition.setPlTime(timeSpinner.getValue());

                            PriceListPosition editedPriceListPosition = priceListPositionRepository.editPriceListPositionPriceAndTime(priceListPosition);
                            if(editedPriceListPosition != null){
                                canExit = true;
                            }
                        }
                        break;
                    case DELETE:
                            canExit = priceListPositionRepository.deletePriceListPosition(priceListPosition.getPlId());
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
            if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT){
                transportCategoryComboBox.requestFocus();
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
}
