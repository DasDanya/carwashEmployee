package ru.pin120.carwashemployee.Bookings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import jfxtras.scene.control.agenda.Agenda;
import jfxtras.scene.control.CalendarPicker;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
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
    private TableView<Box> boxesTable;
    @FXML
    private TableColumn<Box, Long> numberColumn;
    private ObservableList<Box> boxObservableList = FXCollections.observableArrayList();
    private ResourceBundle rb;
    private BoxesRepository boxesRepository = new BoxesRepository();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        calendar.setCalendar(Calendar.getInstance());
        numberColumn.prefWidthProperty().bind(boxesTable.widthProperty());
        numberColumn.setCellValueFactory(new PropertyValueFactory<Box, Long>("boxId"));

        calendarListener();
        setAvailableBoxes();
        settingTooltipForButtons();
        Platform.runLater(()-> {
            getStage().setMaximized(true);
            FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh);
            agenda.setDisplayedLocalDateTime(LocalDateTime.now());
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

    private void setAvailableBoxes(){
        try{
            List<Box> availableBoxes = boxesRepository.getAvailable();
            boxObservableList.setAll(availableBoxes);
            boxesTable.setItems(boxObservableList);

            boxesTable.getSelectionModel().selectFirst();
            Platform.runLater(()-> boxesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            boxesTable.requestFocus();
        }
    }

    private Stage getStage(){
        return (Stage) agenda.getScene().getWindow();
    }

    private Scene getActualScene(){
        return agenda.getScene();
    }

    private void calendarListener(){
        calendar.calendarProperty().addListener((observableValue, oldValue, newValue) -> {
            if(newValue != null){

                LocalDate localDate = newValue.getTime().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                LocalTime nowTime = LocalTime.now();
                agenda.setDisplayedLocalDateTime(LocalDateTime.of(localDate,nowTime));
                // Добавить условие если дата oldValue == newValue, то не обновляем список
//                agenda.setDisplayedLocalDateTime(newValue.getTime().toInstant()
//                        .atZone(ZoneId.systemDefault())
//                        .toLocalDateTime());
                //FXHelper.showInfoAlert(check.toString());

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
        FXHelper.showInfoAlert(boxesTable.getSelectionModel().getSelectedItem().getBoxId() + "");

    }
    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){

    }

}
