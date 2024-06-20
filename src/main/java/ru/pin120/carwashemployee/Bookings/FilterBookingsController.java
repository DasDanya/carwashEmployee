package ru.pin120.carwashemployee.Bookings;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import jfxtras.scene.control.LocalDateTimeTextField;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.StyleHelper;
import ru.pin120.carwashemployee.WorkSchedule.WorkScheduleDTO;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

/**
 * Контроллер для работы с заказами в табличном виде
 */
public class FilterBookingsController implements Initializable {

    @FXML
    private ComboBox<BookingStatus> statusComboBox;
    @FXML
    private ComboBox<Box> boxesComboBox;
    @FXML
    private LocalDateTimeTextField startIntervalField;
    @FXML
    private LocalDateTimeTextField endIntervalField;
    @FXML
    private Button resetStartTimePickerButton;
    @FXML
    private Button resetEndTimePickerButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button showButton;
    @FXML
    private Button calculateButton;
    @FXML
    private Button showFilterParametersButton;
    @FXML
    private Button searchButton;
    @FXML
    private ComboBox<String> operationPriceComboBox;
    @FXML
    private Spinner<Integer> priceSpinner;
    @FXML
    private TableView<BookingFX> bookingsTable;
    public TableColumn<BookingFX, String> bkNumberColumn;
    public TableColumn<BookingFX, String>  startTimeColumn;
    public TableColumn<BookingFX, String>  endTimeColumn;
    public TableColumn<BookingFX, String>  statusColumn;
    public TableColumn<BookingFX, Integer>  priceColumn;
    public TableColumn<BookingFX, Long>  boxColumn;
    public TableColumn<BookingFX, String>  cleanerColumn;
    @FXML
    private Stage stage;
    @FXML
    private Pagination pagination;
    private ResourceBundle rb;

    private Cleaner cleaner;
    private Long clientId;
    private Long cleanerId;

    private List<Booking> bookings = new ArrayList<>();
    private ObservableList<BookingFX> bookingFXES = FXCollections.observableArrayList();
    private BookingsRepository bookingsRepository = new BookingsRepository();
    private BoxesRepository boxesRepository = new BoxesRepository();

    private LocalDateTime filterStartTime;
    private LocalDateTime filterEndTime;
    private Long filterBoxId;
    private String filterStatus;
    private String filterCompareOperator;
    private Integer filterPrice;

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        bkNumberColumn.setCellValueFactory(b->b.getValue().bkIdProperty());
        startTimeColumn.setCellValueFactory(b->b.getValue().startTimeProperty());
        endTimeColumn.setCellValueFactory(b->b.getValue().endTimeProperty());
        statusColumn.setCellValueFactory(b->b.getValue().statusProperty());
        priceColumn.setCellValueFactory(b->b.getValue().priceProperty().asObject());
        boxColumn.setCellValueFactory(b->b.getValue().boxProperty().asObject());
        cleanerColumn.setCellValueFactory(b->b.getValue().cleanerProperty());


        List<String> operators = new ArrayList<>(Arrays.asList(null,"<", ">", "=", "<=", ">=", "!="));
        operationPriceComboBox.getItems().setAll(operators);

        pageIndexListener();
        intervalFieldListeners();
        fillingBoxesComboBox();
        converterBoxesInComboBox();
        settingStatusComboBox();
        settingCountSpinner();

        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doRefresh));
    }

    /**
     * Получение текущей сцены.
     *
     * @return Текущая сцена
     */
    private Scene getActualScene(){
        return bookingsTable.getScene();
    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
    private void setTooltipForButtons() {
        resetStartTimePickerButton.setOnMouseEntered(event -> {
            resetStartTimePickerButton.setTooltip(new Tooltip(rb.getString("RESET_START_TIME")));
        });
        resetEndTimePickerButton.setOnMouseEntered(event -> {
            resetEndTimePickerButton.setTooltip(new Tooltip(rb.getString("RESET_END_TIME")));
        });
        showFilterParametersButton.setOnMouseEntered(event -> {
            showFilterParametersButton.setTooltip(new Tooltip(rb.getString("SHOW_FILTER")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_BOOKING")));
        });
        showButton.setOnMouseEntered(event->{
            showButton.setTooltip(new Tooltip(rb.getString("SHOW_SERVICES")));
        });
        calculateButton.setOnMouseEntered(event->{
            String text = cleanerId == null ? rb.getString("CALCULATE_COST_AND_COUNT") : rb.getString("CALCULATE_COST_AND_COUNT_CLEANER");
            calculateButton.setTooltip(new Tooltip(text));
        });
    }

    /**
     * Настраивает спиннер для выбора целочисленного значения и устанавливает текстовый форматтер для поля редактирования спиннера.
     * Позволяет вводить только цифры в поле редактирования спиннера.
     */
    private void settingCountSpinner() {
        priceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,0,50));
        FXHelper.setContextMenuForEditableTextField(priceSpinner.getEditor());
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                return change;
            }
            return null;
        });

        priceSpinner.getEditor().setTextFormatter(formatter);
    }

    /**
     * Настраивает комбо-бокс для выбора статуса заказа.
     * Добавляет все значения перечисления BookingStatus в комбо-бокс и устанавливает конвертер для отображения текста статуса.
     */
    private void settingStatusComboBox(){
        statusComboBox.getItems().add(null);
        statusComboBox.getItems().addAll(BookingStatus.values());
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

    /**
     * Заполняет комбо-бокс выбора боксов данными из репозитория.
     * В случае ошибки отображает диалоговое окно с сообщением об ошибке.
     */
    private void fillingBoxesComboBox(){
        try{
            List<Box> boxes = boxesRepository.getAll();
            boxesComboBox.getItems().add(null);
            boxesComboBox.getItems().addAll(boxes);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * Устанавливает конвертер для комбо-бокса выбора боксов.
     * Конвертер преобразует объект Box в строку для отображения в комбо-боксе и обратно.
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
     * Добавляет слушатели событий для полей выбора интервала времени.
     * Устанавливает ограничение на ввод текста и обновляет значения полей, обрезая до минут при изменении значения.
     */
    private void intervalFieldListeners(){
        startIntervalField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, Event::consume);
        startIntervalField.localDateTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    LocalDateTime updatedDateTime = newValue.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                    startIntervalField.setLocalDateTime(updatedDateTime);
                }catch (Exception e){}
            }
        });
        endIntervalField.addEventFilter(javafx.scene.input.KeyEvent.KEY_TYPED, Event::consume);
        endIntervalField.localDateTimeProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                try {
                    LocalDateTime updatedDateTime = newValue.truncatedTo(java.time.temporal.ChronoUnit.MINUTES);
                    endIntervalField.setLocalDateTime(updatedDateTime);
                }catch (Exception e){}
            }
        });
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    /**
     * Выполняет операцию обновления данных и сбрасывает все фильтры и настройки.
     * Перезаполняет таблицу заказов с начальной страницы пагинации.
     */
    private void doRefresh(){
        filterStartTime = null;
        filterEndTime = null;
        filterStatus = null;
        filterBoxId = null;
        filterPrice = null;
        filterCompareOperator = null;

        startIntervalField.setLocalDateTime(null);
        endIntervalField.setLocalDateTime(null);
        boxesComboBox.getSelectionModel().selectFirst();
        statusComboBox.getSelectionModel().selectFirst();
        operationPriceComboBox.getSelectionModel().selectFirst();

        fillingTable(0);
        pagination.setCurrentPageIndex(0);
    }

    public void showButtonAction(ActionEvent actionEvent) {
        BookingFX selectedBooking = bookingsTable.getSelectionModel().getSelectedItem();
        if(selectedBooking == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_BOOKING"));
        }else{
            Booking booking = bookings.stream()
                    .filter(b->b.getBkId().equals(selectedBooking.getBkId()))
                    .findFirst()
                    .orElse(null);

            if(booking == null){
                FXHelper.showErrorAlert(rb.getString("BOOKING_NOT_EXISTS"));
            }else{
                try {
                    FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.EditBooking", "Bookings/fxml/EditBooking.fxml");
                    EditBookingController editBookingController = fxWindowData.getLoader().getController();
                    editBookingController.settingForm(null, FXOperationMode.SHOW, fxWindowData.getModalStage(), booking);

                    fxWindowData.getModalStage().showAndWait();
                }catch (Exception e){
                    FXHelper.showErrorAlert(e.getMessage());
                }
            }
        }

        bookingsTable.requestFocus();
    }

    public void showFilterParametersButtonAction(ActionEvent actionEvent) {
        String message = String.format("%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s\n%s: %s",rb.getString("START_INTERVAL"),  filterStartTime == null ? "" : filterStartTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")),  rb.getString("END_INTERVAL"), filterEndTime == null ? "" : filterEndTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), rb.getString("STATUS"), filterStatus == null ? "" : BookingStatus.valueOf(filterStatus).getDisplayValue(),
                rb.getString("BOX"), filterBoxId == null ? "" : filterBoxId, rb.getString("COMPARING_OPERATOR"), filterCompareOperator == null ? "" : filterCompareOperator, rb.getString("PRICE"), filterPrice == null ? "" : filterPrice);
        FXHelper.showInfoAlert(message);
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            filterStartTime = startIntervalField.getLocalDateTime();
            filterEndTime = endIntervalField.getLocalDateTime();
            if(statusComboBox.getSelectionModel().getSelectedItem() != null){
                filterStatus = statusComboBox.getSelectionModel().getSelectedItem().name();
            }
            if(operationPriceComboBox.getSelectionModel().getSelectedItem() != null) {
                filterPrice = priceSpinner.getValue();
                filterCompareOperator = operationPriceComboBox.getSelectionModel().getSelectedItem();
            }
            if(boxesComboBox.getSelectionModel().getSelectedItem() != null){
                filterBoxId = boxesComboBox.getSelectionModel().getSelectedItem().getBoxId();
            }

            fillingTable(0);
            pagination.setCurrentPageIndex(0);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            bookingsTable.requestFocus();
        }
    }


    /**
     * Устанавливает параметры окна на основе переданных данных клиента, мойщика и сцены.
     * В зависимости от переданных объектов клиента или мойщика, устанавливает заголовок окна.
     * Устанавливает всплывающие подсказки для кнопок и заполняет таблицу заказов с начальной страницы пагинации.
     *
     * @param client  Клиент (может быть null).
     * @param cleaner Мойщик (может быть null).
     * @param stage   Сцена (окно) для установки заголовка и работы с всплывающими подсказками.
     */
    public void setParameters(Client client, Cleaner cleaner, Stage stage) {
        this.stage = stage;
        if(client != null) {
            clientId = client.getClId();
            this.stage.setTitle(String.format(rb.getString("FORM_TITLE_CLIENT"), client.getClSurname(), client.getClName()));
        }else if (cleaner != null){
            this.cleaner = cleaner;
            cleanerId = cleaner.getClrId();
            this.stage.setTitle(String.format(rb.getString("FORM_TITLE_CLEANER"), cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic() == null ? "" : cleaner.getClrPatronymic()));
        }else{
            this.stage.setTitle(rb.getString("FORM_TITLE"));
        }

        setTooltipForButtons();
        fillingTable(0);
    }

    /**
     * Устанавливает слушателя изменения текущей страницы пагинации
     * При изменении текущей страницы вызывает метод для заполнения таблицы заказов на новой странице.
     */
    private void pageIndexListener() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    /**
     * Заполняет таблицу заказов на указанной странице пагинации.
     * @param pageIndex Номер страницы пагинации для загрузки данных.
     */
    private void fillingTable(int pageIndex) {
        bookingFXES.clear();
        bookings.clear();
        try{
            bookings = bookingsRepository.get(pageIndex, cleanerId, clientId, filterBoxId, filterStartTime, filterEndTime, filterStatus, filterCompareOperator, filterPrice);
            for(Booking booking :bookings){
                String cleanerData = "";
                Cleaner cleaner = booking.getCleaner();
                if(cleaner != null){
                    cleanerData = String.format("%s %s %s", cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic() == null ? "" : cleaner.getClrPatronymic());
                }
                BookingFX bookingFX = new BookingFX(booking.getBkId(), booking.getBkStartTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), booking.getBkEndTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")), booking.getBkStatus().getDisplayValue(), booking.getBkPrice(), booking.getBox().getBoxId(),cleanerData);
                bookingFXES.add(bookingFX);
            }

            bookingsTable.setItems(bookingFXES);
            bookingsTable.getSelectionModel().selectFirst();
            Platform.runLater(()->bookingsTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            bookingsTable.requestFocus();
        }
    }

    public void resetStartTimePickerButtonAction(ActionEvent actionEvent) {
        startIntervalField.setLocalDateTime(null);
    }

    public void resetEndTimePickerButtonAction(ActionEvent actionEvent) {
        endIntervalField.setLocalDateTime(null);
    }

    public void calculateButtonAction(ActionEvent actionEvent) {
        if(cleanerId == null) {
//            if (filterStartTime == null && filterEndTime == null && filterBoxId == null && filterStatus == null && filterCompareOperator == null && filterPrice == null) {
//                FXHelper.showErrorAlert(rb.getString("NECESSARY_SELECT_FILTER_PARAMETER"));
//            }else{
                try {
                    BookingInfoDTO bookingInfoDTO = bookingsRepository.getInfo(null, clientId, filterBoxId, filterStartTime, filterEndTime, filterStatus, filterCompareOperator, filterPrice);
                    FXHelper.showInfoAlert(String.format(rb.getString("BOOKINGS_INFO"), bookingInfoDTO.getTotalCount(), bookingInfoDTO.getTotalPrice()));
                }catch (Exception e){
                    FXHelper.showErrorAlert(e.getMessage());
                    bookingsTable.requestFocus();
                }

                bookingsTable.requestFocus();
            //}
        }else{
            try {
                if(startIntervalField.getLocalDateTime() == null && endIntervalField.getLocalDateTime() == null){
                    FXHelper.showErrorAlert(rb.getString("NEED_TIME_INTERVAL"));
                }else {
                    Map<LocalDate, BookingInfoDTO> info = bookingsRepository.getInfoAboutWorkOfCleaner(cleanerId, startIntervalField.getLocalDateTime(), endIntervalField.getLocalDateTime());
                    if(info.isEmpty()){
                        FXHelper.showInfoAlert(rb.getString("INFO_WAGES_CLEANER"));
                    }else {
                        generateReport(info);
                    }
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                bookingsTable.requestFocus();
            }
        }
    }

    /**
     * Генерирует Excel-отчет на основе переданной данных о выполненных заказах мойщика.
     *
     * @param info Данные о выполненных заказах мойщика, где ключ - дата, значение - объект BookingInfoDTO.
     * @throws IOException Возникает в случае ошибок при работе с файлами.
     */
    private void generateReport(Map<LocalDate, BookingInfoDTO> info) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(rb.getString("DONE_BOOKINGS"));
        sheet.setColumnWidth(0,  75 * 256);
        sheet.setColumnWidth(1,  30 * 256);
        sheet.setColumnWidth(2,  30 * 256);

        Row headerRow = sheet.createRow(0);
        Cell headerCell = headerRow.createCell(0);
        String patronymic = cleaner.getClrPatronymic() == null ? "" : cleaner.getClrPatronymic();
        headerCell.setCellValue(rb.getString("DONE_BOOKINGS") + " " + cleaner.getClrSurname() + " " + cleaner.getClrName() + " " + patronymic);
        headerCell.setCellStyle(StyleHelper.createStyleBoldText(workbook, false,(short) 12));

        Row intervalRow = sheet.createRow(1);
        Cell with = intervalRow.createCell(0);

        if(startIntervalField.getLocalDateTime() == null && endIntervalField.getLocalDateTime() != null) {
            with.setCellValue(rb.getString("BY") + " " + endIntervalField.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            with.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false, (short) 10));
        }else if(endIntervalField.getLocalDateTime() == null && startIntervalField != null){
            with.setCellValue(rb.getString("WITH") + " " + startIntervalField.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            with.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false, (short) 10));
        }else{
            with.setCellValue(rb.getString("WITH") + " " + startIntervalField.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            with.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false, (short) 10));
            Cell by = intervalRow.createCell(1);
            by.setCellValue(rb.getString("BY") + " " + endIntervalField.getLocalDateTime().format(DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")));
            by.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false, (short) 10));
        }

        Row columns = sheet.createRow(2);
        Cell date = columns.createCell(0);
        date.setCellValue(rb.getString("DATE"));
        date.setCellStyle(StyleHelper.createStyleBoldText(workbook, true, (short) 10));

        Cell countBookings = columns.createCell(1);
        countBookings.setCellValue(rb.getString("COUNT_BOOKINGS"));
        countBookings.setCellStyle(StyleHelper.createStyleBoldText(workbook, true,(short) 10));

        Cell earned = columns.createCell(2);
        earned.setCellValue(rb.getString("EARNED"));
        earned.setCellStyle(StyleHelper.createStyleBoldText(workbook, true,(short) 10));

        int rowIndex = 3;
        int startRowIndex = rowIndex;

        int totalCountBookings = 0;
        int totalEarnedPrice = 0;

        for (Map.Entry<LocalDate, BookingInfoDTO> dayInfo : info.entrySet()) {
            Row dataRow = sheet.createRow(rowIndex);
            Cell dateColumn = dataRow.createCell(0);
            dateColumn.setCellValue(dayInfo.getKey().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
            dateColumn.setCellStyle(StyleHelper.createWithBorder(workbook));

            Cell countColumn = dataRow.createCell(1);
            countColumn.setCellValue(dayInfo.getValue().getTotalCount());
            countColumn.setCellStyle(StyleHelper.createWithBorder(workbook));
            totalCountBookings += dayInfo.getValue().getTotalCount();

            Cell earnedPriceColumn = dataRow.createCell(2);
            earnedPriceColumn.setCellValue(dayInfo.getValue().getTotalPrice());
            earnedPriceColumn.setCellStyle(StyleHelper.createWithBorder(workbook));
            totalEarnedPrice += dayInfo.getValue().getTotalPrice();

            rowIndex++;
        }

        Row total = sheet.createRow(rowIndex);
        Cell totalDaysColumn = total.createCell(0);
        totalDaysColumn.setCellValue(String.format(rb.getString("TOTAL_WORK_DAYS"),rowIndex - startRowIndex));
        totalDaysColumn.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false,(short) 10));

        Cell totalBookingsColumn = total.createCell(1);
        totalBookingsColumn.setCellValue(String.format(rb.getString("TOTAL_BOOKINGS"),totalCountBookings));
        totalBookingsColumn.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false,(short) 10));

        Cell totalEarnedPriceColumn = total.createCell(2);
        totalEarnedPriceColumn.setCellValue(String.format(rb.getString("TOTAL_PRICE"),totalEarnedPrice));
        totalEarnedPriceColumn.setCellStyle(StyleHelper.createStyleBoldItalicText(workbook, false,(short) 10));

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
    }
}
