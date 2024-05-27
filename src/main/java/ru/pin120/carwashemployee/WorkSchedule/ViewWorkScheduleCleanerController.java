package ru.pin120.carwashemployee.WorkSchedule;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.StyleHelper;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

public class ViewWorkScheduleCleanerController implements Initializable {

    @FXML
    private Pagination pagination;
    @FXML
    private DatePicker startIntervalPicker;
    @FXML
    private DatePicker endIntervalPicker;
    @FXML
    private Button getWorkScheduleButton;
    @FXML
    private Button excelButton;
    @FXML
    private TableView<ViewWorkScheduleFX> workScheduleTable;
    @FXML
    private TableColumn<ViewWorkScheduleFX, Long> boxColumn;
    @FXML
    private TableColumn<ViewWorkScheduleFX, String> workDayColumn;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    private Cleaner cleaner;
    private ObservableList<ViewWorkScheduleFX> viewWorkScheduleFXES = FXCollections.observableArrayList();
    private WorkScheduleRepository workScheduleRepository = new WorkScheduleRepository();

    private LocalDate startInterval, endInterval;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        boxColumn.setCellValueFactory(v->v.getValue().boxIdProperty().asObject());
        workDayColumn.setCellValueFactory(v->v.getValue().workDayProperty());
        workScheduleTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        startIntervalPicker.setValue(LocalDate.now());
        endIntervalPicker.setValue(LocalDate.now());
        saveInterval();

        pageIndexListener();
        settingPickers();
        setToolTipForButtons();
    }

    private void setToolTipForButtons() {
        getWorkScheduleButton.setOnMouseEntered(event -> {
            getWorkScheduleButton.setTooltip(new Tooltip(rb.getString("SHOW_WORK_SCHEDULE")));
        });
        excelButton.setOnMouseEntered(event -> {
            excelButton.setTooltip(new Tooltip(rb.getString("EXCEL_BUTTON")));
        });
    }


    private void saveInterval(){
        startInterval = startIntervalPicker.getValue();
        endInterval = endIntervalPicker.getValue();
    }

    private void settingPickers(){
        LocalDate today = LocalDate.now();
        LocalDate firstDayOfNextMonth = today.withDayOfMonth(1).plusMonths(1);

        startIntervalPicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
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
        endIntervalPicker.setDayCellFactory(new Callback<DatePicker, DateCell>() {
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

        startIntervalPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isAfter(endIntervalPicker.getValue())){
                endIntervalPicker.setValue(newValue);
            }
        });

        endIntervalPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue.isBefore(startIntervalPicker.getValue())){
                startIntervalPicker.setValue(newValue);
            }
        });

    }

    private void pageIndexListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    public void settingForm(Cleaner cleaner, Stage modalStage) {
        this.cleaner = cleaner;
        stage = modalStage;

        stage.setTitle(rb.getString("FORM_TITLE") + " " + cleaner.getClrSurname() + " " + cleaner.getClrName() + " " + cleaner.getClrPatronymic());
        fillingTable(0);
    }

    private void fillingTable(int pageIndex){
        viewWorkScheduleFXES.clear();
        try {
            List<WorkScheduleDTO> workScheduleDTOS = workScheduleRepository.get(startInterval, endInterval, cleaner.getClrId(), pageIndex);
            fillingObservableList(workScheduleDTOS);

            workScheduleTable.setItems(viewWorkScheduleFXES);
            workScheduleTable.getSelectionModel().selectFirst();
            Platform.runLater(()->workScheduleTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<WorkScheduleDTO> workScheduleDTOS) {
        for(WorkScheduleDTO workScheduleDTO: workScheduleDTOS){
            ViewWorkScheduleFX viewWorkScheduleFX = new ViewWorkScheduleFX(workScheduleDTO.getBox().getBoxId(), workScheduleDTO.getWsWorkDay());
            viewWorkScheduleFXES.add(viewWorkScheduleFX);
        }
    }

    public void getWorkScheduleButtonAction(ActionEvent actionEvent) {
        saveInterval();
        fillingTable(0);
        pagination.setCurrentPageIndex(0);
    }

    public void excelButtonAction(ActionEvent actionEvent) {
        try{
            List<WorkScheduleDTO> workScheduleDTOS = workScheduleRepository.get(startInterval, endInterval, cleaner.getClrId(),null);
            String startIntervalString = startInterval.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));
            String endIntervalString = endInterval.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"));

            if(!workScheduleDTOS.isEmpty()) {
                Workbook workbook = new XSSFWorkbook();
                Sheet sheet = workbook.createSheet(rb.getString("WORKING_DAYS"));
                sheet.setColumnWidth(0,  75 * 256);
                sheet.setColumnWidth(1,  75 * 256);

                Row headerRow = sheet.createRow(0);
                Cell headerCell = headerRow.createCell(0);
                headerCell.setCellValue(rb.getString("FORM_TITLE") + " " + cleaner.getClrSurname() + " " + cleaner.getClrName() + " " + cleaner.getClrPatronymic());
                headerCell.setCellStyle(StyleHelper.createStyleBoldText(workbook, (short) 14));



                Row intervalRow = sheet.createRow(1);
                Cell with = intervalRow.createCell(0);
                with.setCellValue(rb.getString("WITH") + " " + startIntervalString);
                with.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, (short) 10));

                Cell by = intervalRow.createCell(1);
                by.setCellValue(rb.getString("BY") + " " + startIntervalString);
                by.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, (short) 10));


                Row columns = sheet.createRow(2);
                Cell date = columns.createCell(0);
                date.setCellValue(rb.getString("DATE"));
                date.setCellStyle(StyleHelper.createStyleBoldText(workbook, (short) 10));

                Cell box = columns.createCell(1);
                box.setCellValue(rb.getString("BOX"));
                box.setCellStyle(StyleHelper.createStyleBoldText(workbook, (short) 10));

                int rowIndex = 3;
                int startRowIndex = rowIndex;

                for(WorkScheduleDTO workScheduleDTO: workScheduleDTOS){
                    Row dataRow = sheet.createRow(rowIndex);
                    dataRow.createCell(0).setCellValue(workScheduleDTO.getWsWorkDay().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
                    dataRow.createCell(1).setCellValue(workScheduleDTO.getBox().getBoxId());

                    rowIndex++;
                }

                Row total = sheet.createRow(rowIndex);
                Cell totalColumn = total.createCell(0);
                totalColumn.setCellValue(String.format(rb.getString("COUNT_WORK_DAYS"),rowIndex - startRowIndex));
                totalColumn.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, (short) 10));



                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle(rb.getString("SAVE_DOCUMENT"));
                fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("xlsx file", "*.xlsx"));

                File file = fileChooser.showSaveDialog(stage);
                if (file != null) {
                    try (FileOutputStream fileOut = new FileOutputStream(file)) {
                        workbook.write(fileOut);
                        workbook.close();
                        if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
                            Desktop.getDesktop().open(file);
                        }else{
                            FXHelper.showInfoAlert(String.format(rb.getString("SUCCESS_SAVE"), file.getAbsolutePath()));
                        }
                    }
                }

            }else{
                FXHelper.showErrorAlert(String.format(rb.getString("LIST_WORK_SCHEDULE_EMPTY"), startIntervalString, endIntervalString));
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            workScheduleTable.requestFocus();
        }

        workScheduleTable.requestFocus();
    }
}
