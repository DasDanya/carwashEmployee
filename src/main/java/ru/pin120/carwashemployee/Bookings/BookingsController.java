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
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxFX;
import ru.pin120.carwashemployee.Boxes.BoxStatus;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.SuppliesInBox.DecreaseSuppliesInBoxController;

import java.net.URL;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class BookingsController implements Initializable {

    @FXML
    private CalendarPicker calendar;
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
        getBoxBookings();
        settingTooltipForButtons();
        Platform.runLater(()-> {
            getStage().setMaximized(true);
            FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh);
            agenda.setDisplayedLocalDateTime(LocalDateTime.now());
        });

        settingAgenda();
    }

    private void boxesTableSelectModelListener() {
        boxesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            if(newValue != null){
                getBoxBookings();
            }
        });
    }

    private void settingAgenda(){
        agenda.selectedAppointments().addListener((ListChangeListener<Appointment>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    selectedAppointment = change.getAddedSubList().get(0);
                    FXHelper.showInfoAlert(selectedAppointment.getDescription());
                }
            }
        });

        agenda.addEventFilter(MouseEvent.MOUSE_PRESSED, ev -> {
            if (ev.getButton() == MouseButton.SECONDARY) {
                ev.consume();
            }
        });
    }

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
    }

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

                agenda.setDisplayedLocalDateTime(LocalDateTime.now());
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
    }

    private void addAgendaAppointments(Booking booking){
        ClientsTransport clientsTransport = booking.getClientTransport();
        Client client = clientsTransport.getClient();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

        String summary = String.format(rb.getString("SUMMARY_APPOINTMENT"), booking.getBkId(), booking.getBkStartTime().format(formatter), booking.getBkEndTime().format(formatter), booking.getBkPrice(), clientsTransport.getClTrStateNumber(), client.getClSurname(), client.getClName(), client.getClPhone());
        //String summary = String.format("Заказ №%s\nТранспорт %s", booking.getBkId(), booking.getClientTransport().getClTrStateNumber());
        String groupAgendaColor;
        switch (booking.getBkStatus()){
            case CANCELLED_BY_CLIENT -> groupAgendaColor = "booking-cancelled-by-client";
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


    private Stage getStage(){
        return (Stage) agenda.getScene().getWindow();
    }

    private Scene getActualScene(){
        return agenda.getScene();
    }

    private void calendarListener(){
        calendar.calendarProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null && oldValue != null){

                int oldValueYear = oldValue.get(Calendar.YEAR);
                int oldValueWeek = oldValue.get(Calendar.WEEK_OF_YEAR);

                int newValueYear = newValue.get(Calendar.YEAR);
                int newValueWeek = newValue.get(Calendar.WEEK_OF_YEAR);

                if(oldValueYear == newValueYear && oldValueWeek == newValueWeek){
                    getBoxBookings();
                }

                LocalDate localDate = newValue.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime nowTime = LocalTime.now();
                agenda.setDisplayedLocalDateTime(LocalDateTime.of(localDate,nowTime));


            }
        });
    }

    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    public void editButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.EDIT);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    private void doOperation(FXOperationMode operationMode){
        boolean canShowModalWindow = false;
        BoxFX selectedBoxFX = boxesTable.getSelectionModel().getSelectedItem();
        Booking booking = new Booking();
        if(selectedBoxFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOX"));
        }else {
            switch (operationMode) {
                case CREATE:
                    if (agenda.getDisplayedLocalDateTime().toLocalDate().isBefore(LocalDate.now())) {
                        FXHelper.showErrorAlert(rb.getString("DONT_CREATE_BOOKING_IN_PAST"));
                    } else {
                        if (BoxStatus.valueOfDisplayValue(selectedBoxFX.getBoxStatus()) == BoxStatus.CLOSED) {
                            FXHelper.showErrorAlert(rb.getString("DONT_BOOKED_CLOSED_BOX"));
                        } else {
                            canShowModalWindow = true;
                        }
                    }
                    break;
            }
        }
        if(canShowModalWindow){
            try{
                Box box = boxes.stream()
                        .filter(b->b.getBoxId() == selectedBoxFX.getBoxId())
                        .findFirst()
                        .orElse(null);

                if(box != null){
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Bookings.resources.EditBooking", "Bookings/fxml/EditBooking.fxml", getActualScene());
                    EditBookingController editBookingController = fxWindowData.getLoader().getController();
                    editBookingController.settingForm(agenda.getDisplayedLocalDateTime(), operationMode,fxWindowData.getModalStage(), box, booking);

                    fxWindowData.getModalStage().showAndWait();
                    doResult(operationMode, editBookingController.getExitMode());
                }else{
                    FXHelper.showErrorAlert(rb.getString("BOX_NOT_EXISTS"));
                }

            }catch (Exception e){
                e.printStackTrace();
                FXHelper.showErrorAlert(e.getMessage());
            }
        }

        agenda.requestFocus();
    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    getBoxBookings();
                    break;
            }
        }
    }


    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        setBoxes();
        getBoxBookings();
    }


}
