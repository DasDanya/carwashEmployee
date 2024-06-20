package ru.pin120.carwashemployee.ClientsTransport;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.Transport.Transport;
import ru.pin120.carwashemployee.Transport.TransportFX;
import ru.pin120.carwashemployee.Transport.TransportRepository;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * Контроллер редактирования данных о транспорте клиента
 */
public class EditClientTransportController implements Initializable {


    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<TransportFX, Long> idColumn;
    @FXML
    private TableColumn<TransportFX,String> modelColumn;
    @FXML
    private TableColumn<TransportFX,String> markColumn;
    @FXML
    private TableColumn<TransportFX,String> categoryColumn;
    @FXML
    private TableView<TransportFX> transportsTable;
    @FXML
    private Button searchButton;
    @FXML
    private Button btOK;
    @FXML
    private TextField filterCategoryField;
    @FXML
    private TextField filterModelField;
    @FXML
    private TextField filterMarkField;
    @FXML
    private TextField stateNumberField;
    private ResourceBundle rb;

    private ClientsTransport clientsTransport;
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    @FXML
    private Stage stage;

    private TransportRepository transportRepository = new TransportRepository();
    private ClientsTransportRepository clientsTransportRepository = new ClientsTransportRepository();

    List<Transport> transports;

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(t->t.getValue().trIdProperty().asObject());
        markColumn.setCellValueFactory(t->t.getValue().trMarkProperty());
        modelColumn.setCellValueFactory(t->t.getValue().trModelProperty());
        categoryColumn.setCellValueFactory(t->t.getValue().trCategoryProperty());

        FXHelper.setContextMenuForEditableTextField(filterCategoryField);
        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(stateNumberField);

        stateNumberFieldListener();
        pageIndexListener();
        setTooltipForButton();
    }

    /**
     * Устанавливает слушателя изменения текущей страницы пагинации (pagination).
     * При изменении текущей страницы вызывает метод fillingTable для загрузки данных новой страницы.
     */
    private void pageIndexListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    /**
     * Заполняет таблицу транспорта (transportsTable), исходя из текущего индекса страницы.
     *
     * @param pageIndex индекс текущей страницы пагинации
     */
    private void fillingTable(int pageIndex){
        try{
            transportsTable.getItems().clear();
            if(filterCategoryField.getText().isBlank() && filterMarkField.getText().isBlank() && filterModelField.getText().isBlank()){
                transports = transportRepository.getByPage(pageIndex);
            }else{
                transports = transportRepository.search(pageIndex, filterCategoryField.getText().trim(),filterMarkField.getText().trim(),filterModelField.getText().trim());
            }

            ObservableList<TransportFX> transportFXES = FXCollections.observableArrayList();
            for(Transport transport: transports){
                TransportFX transportFX = new TransportFX(transport.getTrId(), transport.getTrMark(), transport.getTrModel(), transport.getCategoryOfTransport().getCatTrName());
                transportFXES.add(transportFX);
            }

            transportsTable.setItems(transportFXES);
            transportsTable.getSelectionModel().selectFirst();
            Platform.runLater(()->transportsTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            transportsTable.requestFocus();
        }
    }

    /**
     * Устанавливает всплывающую подсказку для кнопки
     */
    private void setTooltipForButton() {
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_TRANSPORT")));
        });
    }

    /**
     * Проверяет длину введенного значения и предотвращает ввод более длинных номеров,
     * чем максимально допустимая длина для государственного номера транспортного средства.
     */
    private void stateNumberFieldListener() {
        stateNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientsTransportFX.MAX_STATE_NUMBER_LENGTH) {
                    stateNumberField.setText(oldValue);
                }
            }
        });
    }

    /**
     * Устанавливает параметры для окна создания, редактирования или удаления транспорта клиента.
     * В зависимости от режима операции (создание, редактирование или удаление),
     * устанавливает заголовок окна и настраивает соответствующие компоненты интерфейса.
     *
     * @param clientsTransport объект типа ClientsTransport, представляющий информацию о транспорте клиента
     * @param operationMode режим операции (CREATE, EDIT или DELETE)
     * @param modalStage модальное окно
     */
    public void setParameters(ClientsTransport clientsTransport, FXOperationMode operationMode, Stage modalStage) {
        this.clientsTransport = clientsTransport;
        this.operationMode = operationMode;
        this.stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                fillingTable(0);
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                fillingComponents();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                stateNumberField.setEditable(false);
                filterMarkField.setEditable(false);
                filterModelField.setEditable(false);
                filterCategoryField.setEditable(false);
                searchButton.setDisable(true);
                transportsTable.setDisable(true);
                pagination.setDisable(true);
                fillingComponents();
                Platform.runLater(()->btOK.requestFocus());
                break;
        }

        closeWindowAction();

    }

    /**
     * Заполняет компоненты интерфейса данными о транспорте клиента для редактирования или удаления.
     * Устанавливает значения полей государственного номера транспорта, марки, модели и категории транспорта.
     * Также заполняет таблицу транспорта
     */
    private void fillingComponents() {
        stateNumberField.setText(clientsTransport.getClTrStateNumber());
        filterMarkField.setText(clientsTransport.getTransport().getTrMark());
        filterModelField.setText(clientsTransport.getTransport().getTrModel());
        filterCategoryField.setText(clientsTransport.getTransport().getCategoryOfTransport().getCatTrName());

        fillingTable(0);
    }


    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            fillingTable(0);
            pagination.setCurrentPageIndex(0);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            transportsTable.requestFocus();
        }
    }

    /**
     * Обработчик события нажатия кнопки "OK".
     *
     * @param actionEvent Событие действия, инициированное нажатием кнопки "OK".
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT) {
            TransportFX transportFX = transportsTable.getSelectionModel().getSelectedItem();
            if (transportFX == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECT_TRANSPORT"));
                transportsTable.requestFocus();
            } else {
                if (stateNumberField.getText() == null || stateNumberField.getText().isBlank()) {
                    FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_STATENUMBER"));
                    stateNumberField.requestFocus();
                } else {
                    String stateNumber = stateNumberField.getText().trim().toUpperCase(Locale.getDefault());
                    boolean validStateNumber = isValidStateNumber(transportFX, stateNumber);
                    if (validStateNumber) {
                        clientsTransport.setClTrStateNumber(stateNumber);
                        Optional<Transport> transport = transports.stream()
                                .filter(t-> t.getTrId() == transportFX.getTrId())
                                .findFirst();
                        transport.ifPresent(t->clientsTransport.setTransport(t));

                        try {
                            if (operationMode == FXOperationMode.CREATE) {
                                ClientsTransport createdTransport = clientsTransportRepository.create(clientsTransport);
                                if(createdTransport != null){
                                    clientsTransport.setClTrId(createdTransport.getClTrId());
                                    canExit = true;
                                }
                            }else{
                                if(clientsTransportRepository.edit(clientsTransport) != null){
                                    canExit = true;
                                }
                            }
                        }catch (Exception e){
                            FXHelper.showErrorAlert(e.getMessage());
                        }
                    }
                }
            }
        }else if(operationMode == FXOperationMode.DELETE){
            try {
                canExit = clientsTransportRepository.delete(clientsTransport.getClTrId());
            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
    }

    /**
     * Проверяет корректность государственного номера транспортного средства
     * в зависимости от категории транспорта.
     *
     * Мотоциклы, квадроциклы и тракторы должны соответствовать шаблону MOTO_AGR_REGEX.
     * Автомобили должны соответствовать шаблону CAR_REGEX.
     * В случае некорректного номера выводится сообщение об ошибке с соответствующим текстом.
     *
     * @param transportFX объект типа TransportFX, содержащий информацию о транспортном средстве
     * @param stateNumber государственный номер транспортного средства для проверки
     * @return true, если государственный номер корректен; в противном случае false
     */
    private boolean isValidStateNumber(TransportFX transportFX, String stateNumber) {
        boolean validStateNumber = true;
        if (transportFX.getTrCategory().toLowerCase().contains("мотоцикл") || transportFX.getTrCategory().toLowerCase().contains("квадроцикл") || transportFX.getTrCategory().toLowerCase().contains("трактор")) {
            if (!stateNumber.matches(ClientsTransportFX.MOTO_AGR_REGEX)) {
                validStateNumber = false;
                FXHelper.showErrorAlert(rb.getString("NOT_VALID_MOTO_AGR"));
            }
        } else if (!stateNumber.matches(ClientsTransportFX.CAR_REGEX)) {
            validStateNumber = false;
            FXHelper.showErrorAlert(rb.getString("NOT_VALID_AUTO"));
        }
        return validStateNumber;
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
