package ru.pin120.carwashemployee.Bookings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalTimeTextField;
import lombok.Getter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransportFX;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransportRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPositionRepository;
import ru.pin120.carwashemployee.PriceListPosition.ServiceInPriceListSelectable;
import ru.pin120.carwashemployee.PriceListPosition.ServiceWithPriceList;
import ru.pin120.carwashemployee.PriceListPosition.ServiceWithPriceListFX;


import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Контроллер редактирования данных о заказе
 */
public class EditBookingController implements Initializable, ServiceInPriceListSelectable {

    @FXML
    private LocalTimeTextField bookingStartTimeField;
    @FXML
    private DatePicker bookingStartDatePicker;
    @FXML
    private ComboBox<Box> boxesComboBox;
    @FXML
    private ComboBox<BookingStatus> statusComboBox;
    @FXML
    private Label infoPriceLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label infoTimeExecuteLabel;
    @FXML
    private CheckBox searchCategoryCheckBox;
    @FXML
    private TextField searchClTransportField;
    @FXML
    private TextField searchServiceField;
    @FXML
    private Button searchClrTransportButton;
    @FXML
    private Button showClientButton;
    @FXML
    private Button searchServButton;
    @FXML
    private Button btOK;
    @FXML
    private TableView<ClientsTransportFX> clientTransportTable;
    @FXML
    private TableColumn<ClientsTransportFX, String> markColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> modelColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> categoryTrColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> stageNumberColumn;
    @FXML
    private TableView<ServiceWithPriceListFX> priceListTable;
    @FXML
    private TableColumn<ServiceWithPriceListFX, CheckBox> selectColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, String> categoryServColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, String> serviceColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, Integer> priceColumn;
    @FXML
    private TableColumn<ServiceWithPriceListFX, Integer> timeColumn;

    private ResourceBundle rb;
    private ClientsTransportRepository clientsTransportRepository = new ClientsTransportRepository();
    private PriceListPositionRepository priceListPositionRepository = new PriceListPositionRepository();
    private BookingsRepository bookingsRepository = new BookingsRepository();
    private BoxesRepository boxesRepository = new BoxesRepository();
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    @FXML
    private Stage stage;
    private Box box;
    private Booking booking;
    private ClientsTransport selectedClientTransport;
    private List<ClientsTransport> clientsTransportList = new ArrayList<>();
    private List<ServiceWithPriceList> serviceWithPriceLists = new ArrayList<>();
    private ObservableList<ClientsTransportFX> clientsTransportFXES = FXCollections.observableArrayList();
    private ObservableList<ServiceWithPriceListFX> serviceWithPriceListFXES =FXCollections.observableArrayList();

    private int price;
    private int timeExecute;
    private LocalDateTime selectedDate;

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        modelColumn.setCellValueFactory(ct->ct.getValue().clTrModelProperty());
        markColumn.setCellValueFactory(ct->ct.getValue().clTrMarkProperty());
        categoryTrColumn.setCellValueFactory(ct->ct.getValue().clTrCategoryProperty());
        stageNumberColumn.setCellValueFactory(ct->ct.getValue().clTrStateNumberProperty());

        selectColumn.setCellValueFactory(p->p.getValue().selectProperty());
        categoryServColumn.setCellValueFactory(p->p.getValue().catNameProperty());
        serviceColumn.setCellValueFactory(p->p.getValue().servNameProperty());
        timeColumn.setCellValueFactory(p->p.getValue().plTimeProperty().asObject());
        priceColumn.setCellValueFactory(p->p.getValue().plPriceProperty().asObject());

        FXHelper.setContextMenuForEditableTextField(searchServiceField);
        FXHelper.setContextMenuForEditableTextField(searchClTransportField);

        clientTransportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        priceListTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        searchClTransportFieldListener();
        tableSelectModelListener();
        setTooltipForButtons();
        converterBoxesInComboBox();
        settingBookingStatusComboBox();
    }

    /**
     * Настраивает конвертер для ComboBox, чтобы правильно отображать и преобразовывать объекты типа Box
     */
    private void converterBoxesInComboBox(){
        boxesComboBox.setConverter(new StringConverter<Box>() {
            @Override
            public String toString(Box box) {
                if(box == null){
                    return "";
                }
                return box.getBoxId().toString();
            }

            @Override
            public Box fromString(String boxIdString) {
                Long boxId = Long.valueOf(boxIdString);
                return null;
            }
        });
    }

    /**
     * Устанавливает слушатель для поля ввода госномера авто
     * Ограничивает длину вводимого текста максимальной допустимой длиной
     */
    private void searchClTransportFieldListener() {
        searchClTransportField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientsTransportFX.MAX_STATE_NUMBER_LENGTH) {
                    searchClTransportField.setText(oldValue);
                }
            }
        });
    }


    /**
     * Устанавливает всплывающие подсказки для кнопок при наведении мыши
     */
    private void setTooltipForButtons() {
        searchClrTransportButton.setOnMouseEntered(event -> {
            searchClrTransportButton.setTooltip(new Tooltip(rb.getString("SEARCH_TRANSPORT")));
        });
        searchServButton.setOnMouseEntered(event -> {
            searchServButton.setTooltip(new Tooltip(rb.getString("SEARCH_SERVICES")));
        });
        showClientButton.setOnMouseEntered(event -> {
            showClientButton.setTooltip(new Tooltip(rb.getString("SHOW_CLIENT_DATA")));
        });
    }

    /**
     * Настраивает форму на основе выбранной даты, режима операции, сцены и заказа
     *
     * @param selectedDate  Выбранная дата
     * @param operationMode Режим работы (создание, редактирование, просмотр и т.д.)
     * @param stage         Stage, связанная с текущей формой
     * @param booking       Заказ
     */
    public void settingForm(LocalDateTime selectedDate, FXOperationMode operationMode, Stage stage, Booking booking){
        this.selectedDate = selectedDate;
        this.operationMode = operationMode;
        this.stage = stage;
        this.box = booking.getBox();
        this.booking = booking;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                boxesComboBox.getItems().add(this.box);
                boxesComboBox.getSelectionModel().selectFirst();
                bookingStartDatePicker.setValue(selectedDate.toLocalDate());
                bookingStartDatePicker.setDisable(true);

                searchServButton.setDisable(true);
                statusComboBox.getItems().add(BookingStatus.BOOKED);
                statusComboBox.getSelectionModel().selectFirst();
                statusComboBox.setDisable(true);
                LocalTime now = LocalTime.now();
                if(now.isBefore(AppHelper.getStartWorkTime()) || !now.isBefore(AppHelper.getEndWorkTime())){
                    bookingStartTimeField.setEditable(false);
                }
                bookingStartTimeField.setLocalTime(now);
                if(this.selectedDate.toLocalDate().equals(LocalDate.now())) {
                    LocalTime startTime = now.minusMinutes(5);
                    if(startTime.isBefore(AppHelper.getStartWorkTime())) {
                        startTime = AppHelper.getStartWorkTime();
                    }
                    settingTimeSpinner(LocalTime.of(startTime.getHour(), startTime.getMinute()));
                }else{
                    settingTimeSpinner(AppHelper.getStartWorkTime());
                }

                setPriceLabelText(price);
                setTimeExecuteLabelText();
                //Platform.runLater(()->searchClTransportField.requestFocus());
                break;
            case EDIT:
                this.stage.setTitle(String.format(rb.getString("EDIT_TITLE"), booking.getBkId()));
                statusComboBox.getItems().add(booking.getBkStatus());
                statusComboBox.getSelectionModel().selectFirst();
                fillingBoxesComboBox();
                boxesComboBox.getSelectionModel().select(box);
                bookingStartTimeField.setLocalTime(LocalTime.of(booking.getBkStartTime().getHour(), booking.getBkStartTime().getMinute()));
                bookingStartDatePicker.setValue(booking.getBkStartTime().toLocalDate());

                showServicesInBooking();
                settingTimeSpinner(AppHelper.getStartWorkTime());
                setLabelBookingInfo(booking);

                break;
            case OTHER:
                statusComboBoxListener();
                this.stage.setTitle(String.format(rb.getString("EDIT_STATUS_TITLE"), booking.getBkId()));

                generalFillingComponents();
                List<BookingStatus> bookingStatuses = getBookingStatuses(booking);
                statusComboBox.getItems().addAll(bookingStatuses);
                statusComboBox.getSelectionModel().selectFirst();

                //if(booking.getBkStatus() != BookingStatus.CANCELLED){
                    //boxesComboBox.getItems().add(this.box);
                //}
                Platform.runLater(this::showServicesInBooking);
                setLabelBookingInfo(booking);
                break;
            case SHOW:
                this.stage.setTitle(String.format(rb.getString("SHOW_TITLE"), booking.getBkId()));
                generalFillingComponents();
                statusComboBox.getItems().add(booking.getBkStatus());
                statusComboBox.getSelectionModel().selectFirst();

                prohibitChangingComponents();
                //LocalTime endTime = LocalTime.of(booking.getBkEndTime().getHour(), booking.getBkEndTime().getMinute());
                //infoTimeExecuteLabel.setText(String.format(rb.getString("INFO_TIME_EXECUTE_VALUE"), endTime.toString()));
                setLabelBookingInfo(booking);
                break;
            case DELETE:
                this.stage.setTitle(String.format(rb.getString("DELETE_TITLE"), booking.getBkId()));
                generalFillingComponents();
                statusComboBox.getItems().add(booking.getBkStatus());
                statusComboBox.getSelectionModel().selectFirst();
                prohibitChangingComponents();

                setLabelBookingInfo(booking);
                break;

        }

        //Platform.runLater(this::statusComboBoxListener);
        this.stage.setMaximized(true);
    }

    /**
     * Устанавливает информацию о заказе в соответствующие компоненты
     *
     * @param booking Заказ
     */
    private void setLabelBookingInfo(Booking booking){
        LocalTime bookingEndTime = LocalTime.of(booking.getBkEndTime().getHour(), booking.getBkEndTime().getMinute());
        infoTimeExecuteLabel.setText(String.format(rb.getString("INFO_TIME_EXECUTE_VALUE"),bookingEndTime));
        setPriceLabelText(booking.getBkPrice());
    }

    /**
     * Заполняет основные компоненты данными из заказа.
     * Устанавливает время и дату начала заказа, а также заполняет ComboBox
     */
    private void generalFillingComponents(){
        bookingStartTimeField.setLocalTime(LocalTime.of(booking.getBkStartTime().getHour(), booking.getBkStartTime().getMinute()));
        bookingStartTimeField.setEditable(false);

        bookingStartDatePicker.setValue(booking.getBkStartTime().toLocalDate());
        bookingStartDatePicker.setDisable(true);

        boxesComboBox.getItems().add(box);
        Platform.runLater(()->boxesComboBox.getSelectionModel().selectFirst());
    }


    /**
     * Возвращает список возможных статусов бронирования на основе текущего статуса
     *
     * @param booking Заказ
     * @return Список возможных статусов бронирования.
     */
    private List<BookingStatus> getBookingStatuses(Booking booking) {
        List<BookingStatus> bookingStatuses = new ArrayList<>();
        if(booking.getBkStatus() == BookingStatus.BOOKED) {
            bookingStatuses = List.of(BookingStatus.IN_PROGRESS, BookingStatus.CANCELLED);
        }else if(booking.getBkStatus() == BookingStatus.CANCELLED){
            bookingStatuses = List.of(BookingStatus.BOOKED);
        }else if(booking.getBkStatus() == BookingStatus.IN_PROGRESS){
            bookingStatuses = List.of(BookingStatus.DONE, BookingStatus.NOT_DONE);
        }
        return bookingStatuses;
    }

    /**
     * Запрещает изменение компонентов формы, устанавливая их в режим только для чтения или отключения
     */
    private void prohibitChangingComponents(){
        showServicesInBooking();
        searchClTransportField.setEditable(false);
        searchServiceField.setEditable(false);
        searchClrTransportButton.setDisable(true);
        searchServButton.setDisable(true);
        priceListTable.setDisable(true);
    }

    /**
     * Разрешает изменение компонентов формы, устанавливая их в режим редактирования или включения.
     */
    private void allowComponentChanges(){
        showServicesInBooking();
        searchClTransportField.setEditable(true);
        searchServiceField.setEditable(true);
        searchClrTransportButton.setDisable(false);
        searchServButton.setDisable(false);
        priceListTable.setDisable(false);
    }

    /**
     * Устанавливает слушатель для ComboBox статусов заказа.
     * Изменяет доступность компонентов на основе выбранного статуса
     */
    private void statusComboBoxListener(){
        statusComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != BookingStatus.BOOKED || booking.getBkStatus() == BookingStatus.CANCELLED){
                prohibitChangingComponents();
            }else{
                allowComponentChanges();
            }
        });
    }

    /**
     * Отображает услуги, связанные с текущим заказом.
     * Устанавливает соответствующие значения в компоненты формы.
     */
    private void showServicesInBooking(){
        searchClTransportField.setText(booking.getClientTransport().getClTrStateNumber());
        searchClientTransport();
        ClientsTransportFX clientsTransportFX = clientsTransportFXES.stream()
                .filter(c->c.getClTrId() == booking.getClientTransport().getClTrId())
                .findFirst()
                .orElse(null);

        if(clientsTransportFX != null){
            Platform.runLater(()->clientTransportTable.getSelectionModel().select(clientsTransportFX));
        }

        for(ServiceWithPriceListFX serviceWithPriceListFX: serviceWithPriceListFXES){
            if(booking.getServices().stream().anyMatch(s->s.getServName().equals(serviceWithPriceListFX.getServName()))){
                serviceWithPriceListFX.getSelect().setSelected(true);
            }
        }
    }


    /**
     * Заполняет ComboBox "boxesComboBox" всеми боксами.
     */
    private void fillingBoxesComboBox(){
        try {
            List<Box> boxes = boxesRepository.getAll();
            boxesComboBox.getItems().setAll(boxes);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * Настраивает Spinner для выбора времени начала заказа.
     * Ограничивает вводимое время рамками рабочего времени.
     *
     * @param startTime Время для сравнения.
     */
    private void settingTimeSpinner(LocalTime startTime){
        bookingStartTimeField.setParseErrorCallback(throwable -> null);

        bookingStartTimeField.localTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && (newValue.isBefore(LocalTime.of(startTime.getHour(), startTime.getMinute())) || !newValue.isBefore(AppHelper.getEndWorkTime()))) {
                bookingStartTimeField.setLocalTime(oldValue);
            }else {
                setTimeExecuteLabelText();
            }
        });
    }

    /**
     * Настраивает ComboBox для выбора статуса заказа с использованием конвертера.
     * Конвертер преобразует статус в строку для отображения.
     */
    private void settingBookingStatusComboBox(){
        statusComboBox.setConverter(new StringConverter<BookingStatus>() {
            @Override
            public String toString(BookingStatus status) {
                return status.getDisplayValue();
            }
            @Override
            public BookingStatus fromString(String string) {
                return null;
            }
        });
    }


    public void searchClrTransportButtonAction(ActionEvent actionEvent) {
        searchClientTransport();
    }

    /**
     * Выполняет поиск транспортного средства клиента по его государственному номеру.
     * Отображает результаты в таблице.
     */
    private void searchClientTransport(){
        if(searchClTransportField.getText() == null || searchClTransportField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("NECCESARY_INPUT_STATE_NUMBER"));
        }else {
            clientsTransportFXES.clear();
            clientsTransportList.clear();
            try {
                clientsTransportList = clientsTransportRepository.getByStateNumber(searchClTransportField.getText().trim());

                for (ClientsTransport ct : clientsTransportList) {
                    ClientsTransportFX clientsTransportFX = new ClientsTransportFX(ct.getClTrId(), ct.getTransport().getTrMark(), ct.getTransport().getTrModel(), ct.getTransport().getCategoryOfTransport().getCatTrName(), ct.getClTrStateNumber());
                    clientsTransportFXES.add(clientsTransportFX);
                }

                clientTransportTable.setItems(clientsTransportFXES);
                if(!clientsTransportFXES.isEmpty()) {
                    clientTransportTable.requestFocus();
                    clientTransportTable.getSelectionModel().selectFirst();
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                clientTransportTable.requestFocus();
            }

        }
    }

    public void showClientButtonAction(ActionEvent actionEvent) {
        if(selectedClientTransport == null){
            FXHelper.showErrorAlert(rb.getString("TRANSPORT_NOT_EXISTS"));
        }else{
            Client client = selectedClientTransport.getClient();
            String clientData = String.format(rb.getString("CLIENT_DATA"), client.getClSurname(), client.getClName(), client.getClPhone(), client.getClDiscount());
            FXHelper.showInfoAlert(clientData);
        }

        clientTransportTable.requestFocus();
    }


    /**
     * Настраивает слушатель изменения выбора в таблице транспортных средств клиентов.
     * Обновляет список услуг.
     */
    private void tableSelectModelListener() {
        clientTransportTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            serviceWithPriceListFXES.clear();
            serviceWithPriceLists.clear();
            searchServButton.setDisable(true);
            price = 0;
            setPriceLabelText(price);
            timeExecute = 0;
            setTimeExecuteLabelText();

            if(newValue != null){
                selectedClientTransport = getSelectedClientTransport(newValue);
                if(selectedClientTransport == null){
                    FXHelper.showErrorAlert(rb.getString("TRANSPORT_NOT_EXISTS"));
                }else{
                    if(selectedClientTransport.getClient().getClDiscount() > 0){
                        infoPriceLabel.setText(String.format(rb.getString("INFO_PRICE_WITH_DISCOUNT"), selectedClientTransport.getClient().getClDiscount()));
                    }else{
                        infoPriceLabel.setText(rb.getString("INFO_PRICE"));
                    }
                    try {
                        serviceWithPriceLists = priceListPositionRepository.getCategoryOfTransportPriceList(selectedClientTransport.getTransport().getCategoryOfTransport().getCatTrId());
                        for(ServiceWithPriceList serviceWithPriceList: serviceWithPriceLists){
                            ServiceWithPriceListFX serviceWithPriceListFX = new ServiceWithPriceListFX(new CheckBox(), serviceWithPriceList.getCatName(), serviceWithPriceList.getServName(), serviceWithPriceList.getPlPrice(), serviceWithPriceList.getPlTime(),this);
                            serviceWithPriceListFXES.add(serviceWithPriceListFX);
                        }
                        priceListTable.setItems(serviceWithPriceListFXES);
                        if(!serviceWithPriceListFXES.isEmpty()) {
                            searchServButton.setDisable(false);
                            //priceListTable.requestFocus();
                            //priceListTable.getSelectionModel().selectFirst();
                        }
                    }catch (Exception e){
                        FXHelper.showErrorAlert(e.getMessage());
                    }
                }
            }
        });
    }

    /**
     * Получает выбранное транспортное средство клиента по его id
     *
     * @param transportFX FX представление транспортного средства клиента
     * @return Объект транспортного средства клиента
     */
    private ClientsTransport getSelectedClientTransport(ClientsTransportFX transportFX){
        return clientsTransportList.stream()
                .filter(c-> c.getClTrId() == transportFX.getClTrId())
                .findFirst()
                .orElse(null);
    }

    public void searchServButtonAction(ActionEvent actionEvent) {
        if(searchServiceField.getText() == null || searchServiceField.getText().isBlank()){
            priceListTable.setItems(serviceWithPriceListFXES);
        }else {
            String parameter = searchServiceField.getText().trim().toLowerCase();
            ObservableList<ServiceWithPriceListFX> searchedServicesWithPriceListFXES = FXCollections.observableArrayList();
            if (searchCategoryCheckBox.isSelected()) {
                searchedServicesWithPriceListFXES = serviceWithPriceListFXES.filtered(s->s.getCatName().toLowerCase().contains(parameter));
            } else {
                searchedServicesWithPriceListFXES = serviceWithPriceListFXES.filtered(s->s.getServName().toLowerCase().contains(parameter));
            }

            List<ServiceWithPriceListFX> sortedList = new ArrayList<>(searchedServicesWithPriceListFXES);
            sortedList.sort(Comparator.comparing(ServiceWithPriceListFX::getCatName, String::compareToIgnoreCase)
                    .thenComparing(ServiceWithPriceListFX::getServName, String::compareToIgnoreCase));


            searchedServicesWithPriceListFXES = FXCollections.observableArrayList(sortedList);
            priceListTable.setItems(searchedServicesWithPriceListFXES);
        }

        priceListTable.getSelectionModel().selectFirst();
        priceListTable.requestFocus();
    }

    /**
     * Обработчик изменения состояния флажка услуги.
     * Обновляет общую цену и время выполнения заказа.
     *
     * @param service Услуга со стоимостью и временем выполнения
     * @param isNowSelected Текущее состояние флажка (выбран или нет).
     */
    @Override
    public void onCheckBoxChanged(ServiceWithPriceListFX service, boolean isNowSelected) {
        if(isNowSelected){
            price += service.getPlPrice();
            timeExecute += service.getPlTime();
        }else{
            price -= service.getPlPrice();
            timeExecute -= service.getPlTime();
        }


        setPriceLabelText(getPriceWithDiscount());
        setTimeExecuteLabelText();
    }

    /**
     * Рассчитывает общую стоимость заказа с учетом скидки клиента.
     *
     * @return Общая стоимость заказа с учетом скидки.
     */
    private int getPriceWithDiscount(){
        double discountedPrice = price * (1 - (selectedClientTransport.getClient().getClDiscount() / 100.0));
        return (int) Math.floor(discountedPrice);
    }


    /**
     * Устанавливает текст лейблу стоимости
     *
     * @param pr Стоимость для отображения
     */
    private void setPriceLabelText(int pr){
        priceLabel.setText(String.format(rb.getString("PRICE_LABEL"), pr));
    }

    /**
     * Устанавливает текст лейблу времени выполнения
     */
    private void setTimeExecuteLabelText(){
        LocalTime totalExecuteTime = bookingStartTimeField.getLocalTime().withSecond(0).withNano(0).plusMinutes(timeExecute);
        infoTimeExecuteLabel.setText(String.format(rb.getString("INFO_TIME_EXECUTE_VALUE"), totalExecuteTime.toString()));
    }

    /**
     * Проверяет, превышает ли общее время выполнения лимит рабочего времени.
     *
     * @return true, если время превышает лимит, иначе false.
     */
    private boolean timeLimitIsExceeding(){
        LocalDateTime maxTime = LocalDateTime.of(bookingStartDatePicker.getValue(), AppHelper.getEndWorkTime());
        LocalDateTime totalTimeExecute = LocalDateTime.of(bookingStartDatePicker.getValue(), bookingStartTimeField.getLocalTime());
        totalTimeExecute = totalTimeExecute.plusMinutes(timeExecute);

        return totalTimeExecute.isAfter(maxTime);
    }

    /**
     * Обработчик нажатия кнопки ОК.
     * Выполняет соответствующие действия в зависимости от режима работы (создание, редактирование, просмотр и т.д.).
     *
     * @param actionEvent Событие действия.
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
            switch (operationMode){
                case CREATE:
                    if(timeLimitIsExceeding()) {
                        FXHelper.showErrorAlert(String.format(rb.getString("EXCEEDING_TIME_LIMIT"), LocalDateTime.of(bookingStartDatePicker.getValue(), AppHelper.getEndWorkTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
                        priceListTable.requestFocus();
                    }else {
                        List<ServiceWithPriceList> services = getListSelectedServices();
                        if (services.isEmpty()) {
                            FXHelper.showErrorAlert(rb.getString("LIST_OF_SERVICES_NOT_EMPTY"));
                        } else {
                            LocalTime startExecuteTime = bookingStartTimeField.getLocalTime();
                            LocalTime totalExecuteTime = startExecuteTime.plusMinutes(timeExecute);
                            BookingDTO bookingDTO = new BookingDTO();
                            bookingDTO.setBkStatus(BookingStatus.BOOKED);
                            bookingDTO.setBkStartTime(getLocalDateTimeWithHoursAndMinutes(selectedDate.withHour(startExecuteTime.getHour()).withMinute(startExecuteTime.getMinute())));
                            bookingDTO.setBkEndTime(getLocalDateTimeWithHoursAndMinutes(selectedDate.withHour(totalExecuteTime.getHour()).withMinute(totalExecuteTime.getMinute())));
                            bookingDTO.setBox(box);
                            bookingDTO.setClientTransport(selectedClientTransport);
                            bookingDTO.setServices(services);

                            try {
                                Booking createdBooking = bookingsRepository.create(bookingDTO);
                                if (createdBooking != null) {
                                    canExit = true;
                                }
                            } catch (Exception e) {
                                FXHelper.showErrorAlert(e.getMessage());
                            }
                        }
                    }
                    break;
                case EDIT:
                    if(timeLimitIsExceeding()) {
                        FXHelper.showErrorAlert(String.format(rb.getString("EXCEEDING_TIME_LIMIT"), LocalDateTime.of(bookingStartDatePicker.getValue(), AppHelper.getEndWorkTime()).format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
                        priceListTable.requestFocus();
                    }else {
                        List<ServiceWithPriceList> services = getListSelectedServices();
                        if (services.isEmpty()) {
                            FXHelper.showErrorAlert(rb.getString("LIST_OF_SERVICES_NOT_EMPTY"));
                        }else{

                            BookingDTO bookingDTO = new BookingDTO();
                            bookingDTO.setBkId(booking.getBkId());
                            bookingDTO.setBkStatus(BookingStatus.BOOKED);

                            LocalTime startExecuteTimeEd = bookingStartTimeField.getLocalTime();
                            LocalTime totalExecuteTimeEd = startExecuteTimeEd.plusMinutes(timeExecute);
                            LocalDateTime localDateTimeStartTime = LocalDateTime.of(bookingStartDatePicker.getValue(), startExecuteTimeEd);
                            LocalDateTime localDateEndStartTime = LocalDateTime.of(bookingStartDatePicker.getValue(), totalExecuteTimeEd);

                            bookingDTO.setBkStartTime(getLocalDateTimeWithHoursAndMinutes(localDateTimeStartTime));
                            bookingDTO.setBkEndTime(getLocalDateTimeWithHoursAndMinutes(localDateEndStartTime));
                            bookingDTO.setBox(boxesComboBox.getSelectionModel().getSelectedItem());
                            bookingDTO.setClientTransport(selectedClientTransport);
                            bookingDTO.setServices(services);
                            try {
                                Booking editedBooking = bookingsRepository.edit(bookingDTO);
                                if (editedBooking != null) {
                                    canExit = true;
                                }
                            } catch (Exception e) {
                                FXHelper.showErrorAlert(e.getMessage());
                            }
                        }
                    }
                    break;
                case OTHER:
                    BookingStatus selectedBookingStatus = statusComboBox.getSelectionModel().getSelectedItem();
                    BookingDTO bookingDTO = null;
                    if(selectedBookingStatus == BookingStatus.IN_PROGRESS){
                        LocalDateTime nowTime = getLocalDateTimeWithHoursAndMinutes(LocalDateTime.now());
                        if(nowTime.isBefore(booking.getBkStartTime()) || nowTime.isAfter(booking.getBkEndTime())){
                            FXHelper.showErrorAlert(String.format(rb.getString("NOT_CORRECT_START_EXECUTING_BOOKING"), booking.getBkStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), booking.getBkEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm"))));
                        }else{
                            bookingDTO = getBookingDTOFromBooking();
                        }
                    }else{
                        bookingDTO = getBookingDTOFromBooking();
                    }
                    if(bookingDTO != null) {
                        if(bookingDTO.getServices().isEmpty()){
                            FXHelper.showErrorAlert(rb.getString("LIST_OF_SERVICES_NOT_EMPTY"));
                            priceListTable.requestFocus();
                        }else{
                            bookingDTO.setBkStatus(selectedBookingStatus);
                            try {
                                Booking editedBooking = bookingsRepository.setNewStatus(bookingDTO);
                                if (editedBooking != null) {
                                    canExit = true;
                                }
                            } catch (Exception e) {
                                FXHelper.showErrorAlert(e.getMessage());
                            }
                        }
                    }

                    break;
                case SHOW:
                    canExit = true;
                    break;
                case DELETE:
                    try {
                        canExit = bookingsRepository.delete(booking.getBkId());
                    }catch (Exception e){
                        FXHelper.showErrorAlert(e.getMessage());
                    }
                    break;
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
    }

    /**
     * Создает объект DTO (Data Transfer Object) для заказа на основе текущего объекта Booking.
     * @return DTO объект заказа
     */
    private BookingDTO getBookingDTOFromBooking(){
        BookingDTO bookingDTO = new BookingDTO();
        bookingDTO.setBkId(booking.getBkId());
        bookingDTO.setBkStartTime(booking.getBkStartTime());
        bookingDTO.setBkEndTime(booking.getBkEndTime());
        bookingDTO.setBox(box);
        bookingDTO.setClientTransport(selectedClientTransport);
        bookingDTO.setServices(getListSelectedServices());

        return bookingDTO;
    }

    /**
     * Возвращает список выбранных услуг из прайс-листа.
     * Перебирает список объектов ServiceWithPriceListFX и добавляет в список услуг те,
     * для которых флажок выбора установлен в состояние "выбран".
     *
     * @return Список выбранных услуг из прайс-листа.
     */
    private List<ServiceWithPriceList> getListSelectedServices(){
        List<ServiceWithPriceList> services = new ArrayList<>();
        for (ServiceWithPriceListFX serviceWithPriceListFX: serviceWithPriceListFXES){
            if(serviceWithPriceListFX.getSelect().isSelected()){
                ServiceWithPriceList service = new ServiceWithPriceList(serviceWithPriceListFX.getCatName(), serviceWithPriceListFX.getServName(), serviceWithPriceListFX.getPlPrice(), serviceWithPriceListFX.getPlTime());
                services.add(service);
            }
        }
        return services;
    }


    /**
     * Возвращает LocalDateTime с обнуленными секундами и наносекундами,
     * используя переданную LocalDateTime.
     *
     * @param localDateTime Исходный LocalDateTime.
     * @return LocalDateTime с обнуленными секундами и наносекундами.
     */
    private LocalDateTime getLocalDateTimeWithHoursAndMinutes(LocalDateTime localDateTime){
        return localDateTime.withSecond(0).withNano(0);
    }

    /**
     * Обработчик нажатия кнопки отмены.
     * Устанавливает режим завершения формы на "Отмена" и закрывает окно.
     *
     * @param actionEvent Событие действия.
     */
    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }
}
