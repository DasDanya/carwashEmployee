package ru.pin120.carwashemployee.Bookings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.agenda.Agenda.Appointment;
import jfxtras.scene.control.CalendarPicker;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxFX;
import ru.pin120.carwashemployee.Boxes.BoxStatus;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Users.UserRole;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * Контроллер формы с заказами
 */
public class BookingsController implements Initializable {

    @FXML
    private Button showTableButton;
    @FXML
    private CalendarPicker calendar;
    @FXML
    private Button changeStatusButton;
    @FXML
    private Button showButton;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Agenda agenda;
    @FXML
    private TableView<BoxFX> boxesTable;
    @FXML
    private TableColumn<BoxFX, Long> numberColumn;
    @FXML
    private TableColumn<BoxFX, String> statusColumn;
    private ObservableList<BoxFX> boxFXES = FXCollections.observableArrayList();
    private ResourceBundle rb;
    private BoxesRepository boxesRepository = new BoxesRepository();
    private BookingsRepository bookingsRepository = new BookingsRepository();
    private List<Box> boxes = new ArrayList<>();
    private List<Booking> bookings = new ArrayList<>();

    private Appointment selectedAppointment;


    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Platform.runLater(()->getActualScene().getStylesheets().add(getClass().getResource("/ru/pin120/carwashemployee/index.css").toExternalForm()));
        rb = resourceBundle;

        calendar.setCalendar(Calendar.getInstance());
        numberColumn.setCellValueFactory(c -> c.getValue().boxIdProperty().asObject());
        statusColumn.setCellValueFactory(c -> c.getValue().boxStatusProperty());
        boxesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        calendarListener();
        boxesTableSelectModelListener();
        setBoxes();
        //getBoxBookings();
        settingTooltipForButtons();
        Platform.runLater(()-> {
            getStage().setMaximized(true);
            FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh);
            agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        });

        settingAgenda();
    }

    /**
     * Добавляет слушатель на таблицу боксов
     */
    private void boxesTableSelectModelListener() {
        boxesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue != null){
                selectedAppointment = null;
                getBoxBookings();
            }
        });
    }

    /**
     * Настройка компонента Agenda
     */
    private void settingAgenda(){
        agenda.selectedAppointments().addListener((ListChangeListener<Appointment>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    selectedAppointment = change.getAddedSubList().get(0);
                }
            }
        });

        agenda.addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                ev.consume();
            }
        });
    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_BOOKING")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_BOOKING")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_BOOKING")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        showButton.setOnMouseEntered(event -> {
            showButton.setTooltip(new Tooltip(rb.getString("SHOW_BOOKING")));
        });
        changeStatusButton.setOnMouseEntered(event -> {
            changeStatusButton.setTooltip(new Tooltip(rb.getString("EDIT_STATUS")));
        });
        showTableButton.setOnMouseEntered(event -> {
            showTableButton.setTooltip(new Tooltip(rb.getString("SHOW_TABLE")));
        });
    }

    /**
     * Загрузка и установка данных о боксах.
     */
    private void setBoxes(){
        try{
            boxFXES.clear();
            boxes = boxesRepository.getAll();
            for(Box box: boxes){
                BoxFX boxFX = new BoxFX(box.getBoxId(), box.getBoxStatus());
                boxFXES.add(boxFX);
            }
            boxesTable.setItems(boxFXES);

            boxesTable.getSelectionModel().selectFirst();
            Platform.runLater(()-> boxesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            boxesTable.requestFocus();
        }
    }


    /**
     * Получение заказов выбранного бокса
     */
    private void getBoxBookings(){
        agenda.appointments().clear();
        BoxFX selectedBox = boxesTable.getSelectionModel().getSelectedItem();
        if(selectedBox == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOX"));
            boxesTable.requestFocus();
        }else{
            LocalDateTime agendaTime = agenda.getDisplayedLocalDateTime();
            LocalDateTime startOfWeek = agendaTime.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                    .withHour(0).withMinute(1).withSecond(0).withNano(0);
            LocalDateTime endOfWeek = agendaTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                    .withHour(23).withMinute(59).withSecond(0).withNano(0);

            try {
                bookings = bookingsRepository.getBoxBookings(startOfWeek, endOfWeek, selectedBox.getBoxId());
                for(Booking booking: bookings){
                    addAgendaAppointments(booking);
                }

                //LocalDateTime localDateTime = LocalDateTime.of(agenda.getDisplayedLocalDateTime().toLocalDate(),
                //agenda.setDisplayedLocalDateTime(LocalDateTime.now());
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
    }

    /**
     * Добавление данных о заказе в Agenda
     * @param booking Заказ
     */
    private void addAgendaAppointments(Booking booking){
        ClientsTransport clientsTransport = booking.getClientTransport();
        Client client = clientsTransport.getClient();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String summary = String.format(rb.getString("SUMMARY_APPOINTMENT"), booking.getBkId(), booking.getBkStartTime().format(formatter), booking.getBkEndTime().format(formatter), booking.getBkPrice(), clientsTransport.getClTrStateNumber(), client.getClSurname(), client.getClName(), client.getClPhone());
        //String summary = String.format("Заказ №%s\nТранспорт %s", booking.getBkId(), booking.getClientTransport().getClTrStateNumber());
        String groupAgendaColor;
        switch (booking.getBkStatus()){
            case CANCELLED -> groupAgendaColor = "booking-cancelled-by-client";
            case DONE -> groupAgendaColor = "booking-done";
            case IN_PROGRESS -> groupAgendaColor = "booking-in-progress";
            case NOT_DONE -> groupAgendaColor = "booking-not-done";
            default -> groupAgendaColor = "booking-booked";
        }
        agenda.appointments().add(new Agenda.AppointmentImplLocal()
                .withStartLocalDateTime(booking.getBkStartTime())
                .withEndLocalDateTime(booking.getBkEndTime())
                .withSummary(summary)
                .withAppointmentGroup(new Agenda.AppointmentGroupImpl().withStyleClass(groupAgendaColor)));
    }

    /**
     * Получение объекта Stage данной формы
     * @return Объект Stage
     */
    private Stage getStage(){
        return (Stage) agenda.getScene().getWindow();
    }

    /**
     * Получение объекта Scene данной формы
     * @return объект Scene
     */
    private Scene getActualScene(){
        return agenda.getScene();
    }

    /**
     * Прослушиватель календаря
     */
    private void calendarListener(){
        calendar.calendarProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){
                LocalDate localDate = newValue.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime nowTime = LocalTime.now();
                agenda.setDisplayedLocalDateTime(LocalDateTime.of(localDate,nowTime));

                if(oldValue != null) {
                    int oldValueYear = oldValue.get(Calendar.YEAR);
                    int oldValueWeek = oldValue.get(Calendar.WEEK_OF_YEAR);

                    int newValueYear = newValue.get(Calendar.YEAR);
                    int newValueWeek = newValue.get(Calendar.WEEK_OF_YEAR);

                    if (oldValueYear != newValueYear || oldValueWeek != newValueWeek) {
                        selectedAppointment = null;
                        getBoxBookings();
                    }
                }else{
                    getBoxBookings();
                }
            }
        });
    }

    /**
     * Обрабатывает действие создания заказа
     *
     * @param actionEvent Событие действия
     */
    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    /**
     * Обрабатывает действие изменения данных о заказе
     *
     * @param actionEvent Событие действия.
     */
    public void editButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.EDIT);
    }

    /**
     * Обрабатывает действие удаления заказа
     *
     * @param actionEvent Событие действия
     */
    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    /**
     * Обрабатывает действие изменения статуса заказа
     *
     * @param actionEvent Событие действия
     */
    public void changeStatusButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.OTHER);
    }


    /**
     * Выполняет операцию с заказом
     *
     * @param operationMode Режим операции
     */
    private void doOperation(FXOperationMode operationMode){
        boolean canOpenWindow = false;
        Booking booking = null;
        switch (operationMode) {
            case CREATE:
                BoxFX selectedBoxFX = boxesTable.getSelectionModel().getSelectedItem();
                if(selectedBoxFX == null){
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOX"));
                }else {
                    Box box = boxes.stream()
                            .filter(b->b.getBoxId() == selectedBoxFX.getBoxId())
                            .findFirst()
                            .orElse(null);

                    if(box != null){
                        if (agenda.getDisplayedLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
                            FXHelper.showErrorAlert(rb.getString("DONT_CREATE_BOOKING_IN_PAST"));
                        } else {
                            if (BoxStatus.valueOfDisplayValue(selectedBoxFX.getBoxStatus()) == BoxStatus.CLOSED) {
                                FXHelper.showErrorAlert(rb.getString("DONT_BOOKED_CLOSED_BOX"));
                            } else {
                                booking = new Booking();
                                booking.setBox(box);
                                canOpenWindow = true;
                            }
                        }
                    }else{
                        FXHelper.showErrorAlert(rb.getString("BOX_NOT_EXISTS"));
                    }
                }
                break;
            case EDIT:
                if(selectedAppointment == null){
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOOKING"));
                    agenda.requestFocus();
                }else {
                    booking = bookings.stream()
                            .filter(b -> b.getBkStartTime().equals(selectedAppointment.getStartLocalDateTime()) && b.getBkEndTime().equals(selectedAppointment.getEndLocalDateTime()))
                            .findFirst()
                            .orElse(null);

                    if (booking == null) {
                        FXHelper.showErrorAlert(rb.getString("BOOKING_NOT_EXISTS"));
                    }else{
                        if(booking.getBkStatus() != BookingStatus.BOOKED){
                            FXHelper.showErrorAlert(String.format(rb.getString("EDIT_BOOKING_ERROR"), BookingStatus.BOOKED.getDisplayValue()));
                        }else{
                            canOpenWindow = true;
                        }
                    }
                }
                break;
            case OTHER:
                if(selectedAppointment == null){
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOOKING"));
                    agenda.requestFocus();
                }else{
                    booking = bookings.stream()
                            .filter(b-> b.getBkStartTime().equals(selectedAppointment.getStartLocalDateTime()) && b.getBkEndTime().equals(selectedAppointment.getEndLocalDateTime()))
                            .findFirst()
                            .orElse(null);

                    if(booking == null){
                        FXHelper.showErrorAlert(rb.getString("BOOKING_NOT_EXISTS"));
                    }else{
                        List<BookingStatus> bookingStatuses = List.of(BookingStatus.NOT_DONE, BookingStatus.DONE);
                        if(bookingStatuses.contains(booking.getBkStatus())){
                            FXHelper.showErrorAlert(String.format(rb.getString("CANNOT_EDIT_BOOKING_WITH_STATUS"), BookingStatus.NOT_DONE.getDisplayValue(), BookingStatus.DONE.getDisplayValue()));
                        }else{
                            canOpenWindow = true;
                        }
                    }
                }
                break;
            case SHOW:
                if(selectedAppointment == null){
                    FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOOKING"));
                    agenda.requestFocus();
                }else {
                    booking = bookings.stream()
                            .filter(b -> b.getBkStartTime().equals(selectedAppointment.getStartLocalDateTime()) && b.getBkEndTime().equals(selectedAppointment.getEndLocalDateTime()))
                            .findFirst()
                            .orElse(null);

                    if (booking == null) {
                        FXHelper.showErrorAlert(rb.getString("BOOKING_NOT_EXISTS"));
                    }else{
                        canOpenWindow = true;
                    }
                }
                break;
            case DELETE:
                if(!AppHelper.getUserInfo().get(2).equals(UserRole.OWNER.name())){
                    FXHelper.showErrorAlert(AppHelper.getCannotAccessOperationText());
                }else {
                    if (selectedAppointment == null) {
                        FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOOKING"));
                        agenda.requestFocus();
                    } else {
                        booking = bookings.stream()
                                .filter(b -> b.getBkStartTime().equals(selectedAppointment.getStartLocalDateTime()) && b.getBkEndTime().equals(selectedAppointment.getEndLocalDateTime()))
                                .findFirst()
                                .orElse(null);

                        if (booking == null) {
                            FXHelper.showErrorAlert(rb.getString("BOOKING_NOT_EXISTS"));
                        } else {
                            if (booking.getBkStatus() == BookingStatus.NOT_DONE || booking.getBkStatus() == BookingStatus.CANCELLED) {
                                canOpenWindow = true;
                            } else {
                                FXHelper.showErrorAlert(String.format(rb.getString("NOT_CORRECT_BOOKING_FOR_DELETE"), BookingStatus.CANCELLED.getDisplayValue(), BookingStatus.NOT_DONE.getDisplayValue()));
                            }
                        }
                    }
                }
                break;

        }
        if(canOpenWindow){
            try{
                FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.EditBooking", "Bookings/fxml/EditBooking.fxml");
                EditBookingController editBookingController = fxWindowData.getLoader().getController();
                editBookingController.settingForm(agenda.getDisplayedLocalDateTime(), operationMode,fxWindowData.getModalStage(), booking);

                fxWindowData.getModalStage().showAndWait();
                reloadBookings();
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }

        agenda.requestFocus();
    }

    /**
     * Обновляет данные о заказах
     */
    private void reloadBookings() {
        selectedAppointment = null;
        getBoxBookings();
    }

//    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode) {
//
//    }


    /**
     * Обрабатывает действие обновления данных
     *
     * @param actionEvent Событие действия.
     */
    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    /**
     * Выполняет обновление данных
     */
    private void doRefresh(){
        setBoxes();
        reloadBookings();
    }

    /**
     * Обрабатывает действие показа заказа
     *
     * @param actionEvent Событие действия
     */
    public void showButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.SHOW);
    }

    /**
     * Обрабатывает действие нажатия на кнопку открытия табличного представления
     *
     * @param actionEvent Событие действия
     */
    public void showTableButtonAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.FilterBookings", "Bookings/fxml/FilterBookings.fxml");
            FilterBookingsController filterBookingsController = fxWindowData.getLoader().getController();
            filterBookingsController.setParameters(null, null, fxWindowData.getModalStage());
            fxWindowData.getModalStage().showAndWait();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

    }
}
