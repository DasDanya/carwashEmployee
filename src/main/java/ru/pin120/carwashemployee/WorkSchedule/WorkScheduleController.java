package ru.pin120.carwashemployee.WorkSchedule;


import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Cleaners.*;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.util.Callback;
import ru.pin120.carwashemployee.FX.FXWindowData;

/**
 * Контроллер формы с рабочими днями
 */
public class WorkScheduleController implements Initializable {
    @FXML
    private ComboBox<Box> boxesComboBox;
    @FXML
    private Button refreshButton;
    @FXML
    private Button getWorkScheduleCleanerButton;
    @FXML
    private Button btSave;
    @FXML
    private TableView<WorkScheduleFX> workScheduleTable;
    @FXML
    private TableColumn<WorkScheduleFX, Long> cleanerIdColumn;
    @FXML
    private TableColumn<WorkScheduleFX, String> fioColumn;
    @FXML
    private TableColumn<WorkScheduleFX, String> statusColumn;
    @FXML
    private TableColumn<WorkScheduleFX, String> phoneColumn;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day1Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day2Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day3Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day4Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day5Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day6Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day7Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day8Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day9Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day10Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day11Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day12Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day13Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day14Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day15Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day16Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day17Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day18Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day19Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day20Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day21Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day22Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day23Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day24Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day25Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day26Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day27Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day28Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day29Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day30Column;
    @FXML
    private TableColumn<WorkScheduleFX, CheckBox> day31Column;

    @FXML
    private DatePicker datePicker;
    private ResourceBundle rb;

    private LocalDate startInterval, endInterval;

    private CleanersRepository cleanersRepository = new CleanersRepository();
    private WorkScheduleRepository workScheduleRepository = new WorkScheduleRepository();
    private BoxesRepository boxesRepository = new BoxesRepository();
    private List<CleanerDTO> cleanerDTOList;

    private ObservableList<WorkScheduleFX> workScheduleFXES = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        Platform.runLater(()->getActualScene().getStylesheets().add(getClass().getResource("/ru/pin120/carwashemployee/index.css").toExternalForm()));

        cleanerIdColumn.setCellValueFactory(w->w.getValue().clrIdProperty().asObject());
        fioColumn.setCellValueFactory(w->w.getValue().fioProperty());
        statusColumn.setCellValueFactory(w->w.getValue().statusProperty());
        phoneColumn.setCellValueFactory(w->w.getValue().phoneProperty());
        day1Column.setCellValueFactory(w->w.getValue().day1Property());
        day2Column.setCellValueFactory(w->w.getValue().day2Property());
        day3Column.setCellValueFactory(w->w.getValue().day3Property());
        day4Column.setCellValueFactory(w->w.getValue().day4Property());
        day5Column.setCellValueFactory(w->w.getValue().day5Property());
        day6Column.setCellValueFactory(w->w.getValue().day6Property());
        day7Column.setCellValueFactory(w->w.getValue().day7Property());
        day8Column.setCellValueFactory(w->w.getValue().day8Property());
        day9Column.setCellValueFactory(w->w.getValue().day9Property());
        day10Column.setCellValueFactory(w->w.getValue().day10Property());
        day11Column.setCellValueFactory(w->w.getValue().day11Property());
        day12Column.setCellValueFactory(w->w.getValue().day12Property());
        day13Column.setCellValueFactory(w->w.getValue().day13Property());
        day14Column.setCellValueFactory(w->w.getValue().day14Property());
        day15Column.setCellValueFactory(w->w.getValue().day15Property());
        day16Column.setCellValueFactory(w->w.getValue().day16Property());
        day17Column.setCellValueFactory(w->w.getValue().day17Property());
        day18Column.setCellValueFactory(w->w.getValue().day18Property());
        day19Column.setCellValueFactory(w->w.getValue().day19Property());
        day20Column.setCellValueFactory(w->w.getValue().day20Property());
        day21Column.setCellValueFactory(w->w.getValue().day21Property());
        day22Column.setCellValueFactory(w->w.getValue().day22Property());
        day23Column.setCellValueFactory(w->w.getValue().day23Property());
        day24Column.setCellValueFactory(w->w.getValue().day24Property());
        day25Column.setCellValueFactory(w->w.getValue().day25Property());
        day26Column.setCellValueFactory(w->w.getValue().day26Property());
        day27Column.setCellValueFactory(w->w.getValue().day27Property());
        day28Column.setCellValueFactory(w->w.getValue().day28Property());
        day29Column.setCellValueFactory(w->w.getValue().day29Property());
        day30Column.setCellValueFactory(w->w.getValue().day30Property());
        day31Column.setCellValueFactory(w->w.getValue().day31Property());

        settingDatePicker();
        settingBoxesComboBox();
        filling();
        //fillingTable();
        setTooltipForButtons();

        Platform.runLater(()->FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doRefresh));

    }

    private void setTooltipForButtons() {
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        getWorkScheduleCleanerButton.setOnMouseEntered(event -> {
            getWorkScheduleCleanerButton.setTooltip(new Tooltip(rb.getString("SHOW_WORK_SCHEDULE_CLEANER")));
        });
    }

    private void settingBoxesComboBox(){
        boxesComboBox.setConverter(new StringConverter<Box>() {
            @Override
            public String toString(Box box) {
                return box.getBoxId().toString();
            }
            @Override
            public Box fromString(String string) {
                return null;
            }
        });

        boxesComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null && oldValue != null){
                if(!Objects.equals(newValue.getBoxId(), oldValue.getBoxId())){
                    LocalDate today = LocalDate.now();
                    LocalDate pickerValue = datePicker.getValue();
                    fillingTable(today.getMonth() == pickerValue.getMonth() && today.getYear() == pickerValue.getYear());
                }
            }
        });
    }


    private void filling(){
        try{
            List<Box> boxes = boxesRepository.getAll();

            boxesComboBox.getItems().setAll(boxes);
            boxesComboBox.getSelectionModel().selectFirst();

            if(boxesComboBox.getItems().isEmpty()){
                btSave.setDisable(true);
            }else{
                fillingTable(true);
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }



    private Scene getActualScene(){
        return workScheduleTable.getScene();
    }

    private void fillingTable(boolean currentMonth) {
        setInterval(datePicker.getValue());
        try {
            //workSchedules = workScheduleRepository.get(startInterval, endInterval, boxesComboBox.getValue().getBoxId());
            cleanerDTOList = cleanersRepository.getWithWorkSchedule(startInterval, endInterval, boxesComboBox.getValue().getBoxId(), currentMonth);

            workScheduleFXES.clear();
            fillingObservableList();
            workScheduleTable.setItems(workScheduleFXES);

            workScheduleTable.getSelectionModel().selectFirst();
            Platform.runLater(() -> workScheduleTable.requestFocus());
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }

    }

    private void fillingObservableList(){
        int daysInMonth = datePicker.getValue().lengthOfMonth();
        for(CleanerDTO cleanerDTO: cleanerDTOList){
            String patronymic = cleanerDTO.getClrPatronymic() == null ? "" : cleanerDTO.getClrPatronymic();
            String fio = cleanerDTO.getClrSurname() + " " + cleanerDTO.getClrName() + " " + patronymic;
            WorkScheduleFX workScheduleFX = new WorkScheduleFX(cleanerDTO.getClrId(), fio, cleanerDTO.getClrStatus().getDisplayValue(), cleanerDTO.getClrPhone());

            boolean dismissed =  cleanerDTO.getClrStatus() == CleanerStatus.DISMISSED;
            for(int day = 1; day <= daysInMonth; day++){
                int finalDay = day;
                LocalDate date = LocalDate.of(datePicker.getValue().getYear(), datePicker.getValue().getMonth(), finalDay);
                //boolean disabled = dismissed || date.isBefore(LocalDate.now()) || date.lengthOfMonth() < day;

                boolean disabled = dismissed || date.isBefore(LocalDate.now());
                boolean selected = cleanerDTO.getWorkSchedules().stream().anyMatch(ws -> ws.getWsWorkDay().getDayOfMonth() == finalDay);
                settingUpDisplayOfDays(workScheduleFX, finalDay, disabled, selected);
            }
            // для месяцев, у которых количество дней < 31
            for(int remainDays = daysInMonth + 1; remainDays <= 31; remainDays++){
                settingUpDisplayOfDays(workScheduleFX,remainDays,true, false);
            }


//            for (WorkSchedule workSchedule: cleanerDTO.getWorkSchedules()){
//                int dayOfMonth = workSchedule.getWsWorkDay().getDayOfMonth();
//                setDaySelected(workScheduleFX, dayOfMonth);
//            }
            workScheduleFXES.add(workScheduleFX);
        }
    }

    private void settingUpDisplayOfDays(WorkScheduleFX workScheduleFX, int day, boolean disabled, boolean selected) {
        switch (day) {
            case 1:
                workScheduleFX.getDay1().setDisable(disabled);
                workScheduleFX.getDay1().setSelected(selected);
                break;
            case 2:
                workScheduleFX.getDay2().setDisable(disabled);
                workScheduleFX.getDay2().setSelected(selected);
                break;
            case 3:
                workScheduleFX.getDay3().setDisable(disabled);
                workScheduleFX.getDay3().setSelected(selected);
                break;
            case 4:
                workScheduleFX.getDay4().setDisable(disabled);
                workScheduleFX.getDay4().setSelected(selected);
                break;
            case 5:
                workScheduleFX.getDay5().setDisable(disabled);
                workScheduleFX.getDay5().setSelected(selected);
                break;
            case 6:
                workScheduleFX.getDay6().setDisable(disabled);
                workScheduleFX.getDay6().setSelected(selected);
                break;
            case 7:
                workScheduleFX.getDay7().setDisable(disabled);
                workScheduleFX.getDay7().setSelected(selected);
                break;
            case 8:
                workScheduleFX.getDay8().setDisable(disabled);
                workScheduleFX.getDay8().setSelected(selected);
                break;
            case 9:
                workScheduleFX.getDay9().setDisable(disabled);
                workScheduleFX.getDay9().setSelected(selected);
                break;
            case 10:
                workScheduleFX.getDay10().setDisable(disabled);
                workScheduleFX.getDay10().setSelected(selected);
                break;
            case 11:
                workScheduleFX.getDay11().setDisable(disabled);
                workScheduleFX.getDay11().setSelected(selected);
                break;
            case 12:
                workScheduleFX.getDay12().setDisable(disabled);
                workScheduleFX.getDay12().setSelected(selected);
                break;
            case 13:
                workScheduleFX.getDay13().setDisable(disabled);
                workScheduleFX.getDay13().setSelected(selected);
                break;
            case 14:
                workScheduleFX.getDay14().setDisable(disabled);
                workScheduleFX.getDay14().setSelected(selected);
                break;
            case 15:
                workScheduleFX.getDay15().setDisable(disabled);
                workScheduleFX.getDay15().setSelected(selected);
                break;
            case 16:
                workScheduleFX.getDay16().setDisable(disabled);
                workScheduleFX.getDay16().setSelected(selected);
                break;
            case 17:
                workScheduleFX.getDay17().setDisable(disabled);
                workScheduleFX.getDay17().setSelected(selected);
                break;
            case 18:
                workScheduleFX.getDay18().setDisable(disabled);
                workScheduleFX.getDay18().setSelected(selected);
                break;
            case 19:
                workScheduleFX.getDay19().setDisable(disabled);
                workScheduleFX.getDay19().setSelected(selected);
                break;
            case 20:
                workScheduleFX.getDay20().setDisable(disabled);
                workScheduleFX.getDay20().setSelected(selected);
                break;
            case 21:
                workScheduleFX.getDay21().setDisable(disabled);
                workScheduleFX.getDay21().setSelected(selected);
                break;
            case 22:
                workScheduleFX.getDay22().setDisable(disabled);
                workScheduleFX.getDay22().setSelected(selected);
                break;
            case 23:
                workScheduleFX.getDay23().setDisable(disabled);
                workScheduleFX.getDay23().setSelected(selected);
                break;
            case 24:
                workScheduleFX.getDay24().setDisable(disabled);
                workScheduleFX.getDay24().setSelected(selected);
                break;
            case 25:
                workScheduleFX.getDay25().setDisable(disabled);
                workScheduleFX.getDay25().setSelected(selected);
                break;
            case 26:
                workScheduleFX.getDay26().setDisable(disabled);
                workScheduleFX.getDay26().setSelected(selected);
                break;
            case 27:
                workScheduleFX.getDay27().setDisable(disabled);
                workScheduleFX.getDay27().setSelected(selected);
                break;
            case 28:
                workScheduleFX.getDay28().setDisable(disabled);
                workScheduleFX.getDay28().setSelected(selected);
                break;
            case 29:
                workScheduleFX.getDay29().setDisable(disabled);
                workScheduleFX.getDay29().setSelected(selected);
                break;
            case 30:
                workScheduleFX.getDay30().setDisable(disabled);
                workScheduleFX.getDay30().setSelected(selected);
                break;
            case 31:
                workScheduleFX.getDay31().setDisable(disabled);
                workScheduleFX.getDay31().setSelected(selected);
                break;
            default:
                throw new IllegalArgumentException("Неверный день: " + day);
        }
    }

    private void setInterval(LocalDate date){
        YearMonth month = YearMonth.from(date);
        startInterval = month.atDay(1);
        endInterval = month.atEndOfMonth();
    }

    private void highlightColumn(int day){
        workScheduleTable.getColumns().forEach(column -> column.getStyleClass().remove("highlight-header"));
        String idColumn = "day"+day+"Column";
        Optional<TableColumn<WorkScheduleFX, ?>> foundColumn = workScheduleTable.getColumns().stream()
                .filter(col -> col.getId().equals(idColumn))
                .findFirst();

        foundColumn.ifPresent(c->c.getStyleClass().add("highlight-header"));
    }

    private void settingDatePicker(){
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfNextMonth = today.withDayOfMonth(1).plusMonths(1);
        datePicker.setValue(today);
        highlightColumn(datePicker.getValue().getDayOfMonth());
        datePicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
            @Override
            public DateCell call(final DatePicker datePicker) {
                return new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (item.isAfter(firstDayOfNextMonth.minusDays(1))) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                };
            }
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            highlightColumn(newValue.getDayOfMonth());
            if(oldValue.getMonth() != newValue.getMonth() || oldValue.getYear() != newValue.getYear()){
                //setInterval(newValue);
                LocalDate now = LocalDate.now();
                if(now.getYear() == newValue.getYear() && now.getMonth() == newValue.getMonth()) {
                    fillingTable(true);
                    btSave.setDisable(false);
                }else{
                    fillingTable(false);
                    btSave.setDisable(true);
                }
            }
        });
    }


    public void btSaveAction(ActionEvent actionEvent) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        List<CleanerDTO> created = new ArrayList<>();
        List<WorkSchedule> deleted = new ArrayList<>();
        LocalDate today = LocalDate.now();
        for(int i = 0; i < workScheduleFXES.size(); i++) {

            WorkScheduleFX workScheduleFX = workScheduleFXES.get(i);
            CleanerDTO cleanerDTO = cleanerDTOList.get(i);
            Class<?> clazz = workScheduleFX.getClass();

            for (int day = today.getDayOfMonth(); day <= datePicker.getValue().lengthOfMonth(); day++) {
                int finalDay = day;
                String getterName = "getDay" + finalDay;
                Method method = clazz.getMethod(getterName);
                CheckBox check = (CheckBox) method.invoke(workScheduleFX);

                WorkSchedule workSchedule = cleanerDTO.getWorkSchedules().stream()
                        .filter(ws->ws.getWsWorkDay().getDayOfMonth() == finalDay)
                        .findFirst()
                        .orElse(null);

                if(!check.isSelected() && workSchedule != null){ // Если день отжали (при инициализации была галка)
                   deleted.add(workSchedule);
                }

            }
            //res += "\n";
        }

        for(int i = 0; i < workScheduleFXES.size(); i++) {
            WorkScheduleFX workScheduleFX = workScheduleFXES.get(i);
            CleanerDTO cleanerDTO = cleanerDTOList.get(i);
            Class<?> clazz = workScheduleFX.getClass();

            List<WorkSchedule> createdWorkSchedules = new ArrayList<>();

            for (int day = today.getDayOfMonth(); day <= datePicker.getValue().lengthOfMonth(); day++) {
                int finalDay = day;
                String getterName = "getDay" + finalDay;
                Method method = clazz.getMethod(getterName);
                CheckBox check = (CheckBox) method.invoke(workScheduleFX);

                WorkSchedule workSchedule = cleanerDTO.getWorkSchedules().stream()
                        .filter(ws -> ws.getWsWorkDay().getDayOfMonth() == finalDay)
                        .findFirst()
                        .orElse(null);

                LocalDate date = LocalDate.of(today.getYear(), today.getMonth(), finalDay);
                if(check.isSelected() && workSchedule == null) { // Если день выбрали (при инициализации не было галки)
                    CleanerDTO cleanerInCreatedList = getByBoxIdAndDayInWorkSchedule(created, deleted, finalDay);
                    if (checkExistingWorkingDay(cleanerInCreatedList, date)) {
                        return;
                    } else {
                        CleanerDTO cleanerInBaseList = getByBoxIdAndDayInWorkSchedule(cleanerDTOList, deleted, finalDay);
                        if (checkExistingWorkingDay(cleanerInBaseList, date)) {
                            return;
                        }

                    }

                    WorkSchedule createdWorkSchedule = new WorkSchedule();
                    createdWorkSchedule.setWsWorkDay(date);
                    createdWorkSchedules.add(createdWorkSchedule);
                }
            }

            if(!createdWorkSchedules.isEmpty()){
                CleanerDTO newCleaner = new CleanerDTO(cleanerDTO.getClrId(), cleanerDTO.getClrSurname(), cleanerDTO.getClrName(), cleanerDTO.getClrPatronymic(),cleanerDTO.getClrPhone(), cleanerDTO.getClrStatus(), boxesComboBox.getValue(), createdWorkSchedules);
                created.add(newCleaner);
            }
        }

        boolean success = true;
        try {
            if (!created.isEmpty()) {
                ResultCreateWorkSchedulesDTO result = workScheduleRepository.create(created);
                if(result == null){
                    success = false;
                }
            }
            if(!deleted.isEmpty()){
                success = workScheduleRepository.delete(deleted);
            }
        }catch (Exception e){
            success = false;
            FXHelper.showErrorAlert(e.getMessage());
            workScheduleTable.requestFocus();
        }

        if(success) {
            FXHelper.showInfoAlert(rb.getString("SUCCESS_SAVE"));
        }
        workScheduleFXES.clear();
        fillingTable(true);



        //System.out.println(created + "\n" + deleted);
        //System.out.println(res);
    }


    private boolean checkExistingWorkingDay(CleanerDTO cleaner, LocalDate date){
        if(cleaner != null){
            FXHelper.showErrorAlert(String.format(rb.getString("EXIST_WORKSCHEDULE_MESSAGE"), cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic() == null ? "" : cleaner.getClrPatronymic(), date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))));
            return true;
        }
        return false;
    }

    private CleanerDTO getByBoxIdAndDayInWorkSchedule(List<CleanerDTO> cleanerDTOList, List<WorkSchedule> deleted, int day){
        return  cleanerDTOList.stream()
                .filter(c-> c.getWorkSchedules().stream().anyMatch(ws->ws.getWsWorkDay().getDayOfMonth() == day && deleted.stream().noneMatch(w->w.getWsId().equals(ws.getWsId()))))
                .findFirst()
                .orElse(null);


//        LocalDate now = LocalDate.now();
//        for(CleanerDTO cleanerDTO1: cleanerDTOList){
//            if(cleanerDTO1.getBox().getBoxId().longValue() == cleanerDTO.getBox().getBoxId().longValue() && cleanerDTO1.getClrId().longValue() != cleanerDTO.getClrId().longValue()){
//                for(WorkSchedule workSchedule: cleanerDTO1.getWorkSchedules()){
//                    if(workSchedule.getWsWorkDay().getDayOfMonth() == day){
//                        return cleanerDTO1;
//                    }
//                }
//            }
//        }

        //return null;
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        LocalDate pastDate = datePicker.getValue();
        LocalDate now = LocalDate.now();
        datePicker.setValue(now);
        if(pastDate.getMonth() == now.getMonth() && pastDate.getYear() == now.getYear()) {
            fillingTable(true);
        }
    }

    public void getWorkScheduleCleanerButtonAction(ActionEvent actionEvent) {
        if(workScheduleTable.getSelectionModel().getSelectedItem() == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CLEANER"));
        }else{
            WorkScheduleFX workScheduleFX = workScheduleTable.getSelectionModel().getSelectedItem();
            try {
                Cleaner cleaner = new Cleaner();
                cleaner.setClrId(workScheduleFX.getClrId());
                String[] fio = workScheduleFX.getFio().split(" ");
                cleaner.setClrSurname(fio[0]);
                cleaner.setClrName(fio[1]);
                cleaner.setClrPatronymic(fio.length == 3 ? fio[2] : "");

                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.WorkSchedule.resources.ViewWorkScheduleCleaner", "WorkSchedule/fxml/ViewWorkScheduleCleaner.fxml", getActualScene());
                ViewWorkScheduleCleanerController viewWorkScheduleCleanerController = fxWindowData.getLoader().getController();
                viewWorkScheduleCleanerController.settingForm(cleaner,fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }

        workScheduleTable.requestFocus();
    }
}
