package ru.pin120.carwashemployee.Cleaners;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.util.StringConverter;
import ru.pin120.carwashemployee.Bookings.FilterBookingsController;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Clients.ClientFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.WorkSchedule.ViewWorkScheduleCleanerController;
import ru.pin120.carwashemployee.WorkSchedule.WorkScheduleFX;

import java.net.URL;
import java.util.*;

/**
 * Контроллер формы с мойщиками
 */
public class CleanersController implements Initializable {

    @FXML
    private Button showBookingsButton;
    @FXML
    private Button getWorkScheduleCleanerButton;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button showPhotoButton;
    @FXML
    private TextField filterSurnameField;
    @FXML
    private TextField filterNameField;
    @FXML
    private TextField filterPatronymicField;
    @FXML
    private TextField filterPhoneField;
    @FXML
    private ComboBox<CleanerStatus> filterStatusComboBox;
    @FXML
    private TableView<CleanerFX> cleanersTable;
    @FXML
    private TableColumn<CleanerFX,String> surnameColumn;
    @FXML
    private TableColumn<CleanerFX,String> nameColumn;
    @FXML
    private TableColumn<CleanerFX,String> patronymicColumn;
    @FXML
    private TableColumn<CleanerFX,String> statusColumn;
    @FXML
    private TableColumn<CleanerFX,String> phoneColumn;
    @FXML
    private TableColumn<CleanerFX,Long> idColumn;
    private ResourceBundle rb;
    private CleanersRepository cleanersRepository = new CleanersRepository();
    private ObservableList<CleanerFX> cleanerFXES = FXCollections.observableArrayList();


    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        surnameColumn.setCellValueFactory(c -> c.getValue().clrSurnameProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().clrNameProperty());
        patronymicColumn.setCellValueFactory(c -> c.getValue().clrPatronymicProperty());
        phoneColumn.setCellValueFactory(c -> c.getValue().clrPhoneProperty());
        statusColumn.setCellValueFactory(c -> c.getValue().clrStatusProperty());
        idColumn.setCellValueFactory(c -> c.getValue().clrIdProperty().asObject());
        cleanersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FXHelper.setContextMenuForEditableTextField(filterSurnameField);
        FXHelper.setContextMenuForEditableTextField(filterNameField);
        FXHelper.setContextMenuForEditableTextField(filterPatronymicField);
        FXHelper.setContextMenuForEditableTextField(filterPhoneField);

        fillingAll();

        setConvertersForComboBoxes();
        settingTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
        filterPhoneListener();
    }

    /**
     * Устанавливает слушатель изменений текстового поля для фильтрации ввода телефонного номера.
     * Метод удаляет все нечисловые символы из введенного значения и обрезает его до максимально допустимой длины,
     */
    private void filterPhoneListener(){
        filterPhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String digitsOnly = newValue.replaceAll("\\D", "");

                if (digitsOnly.length() > CleanerFX.MAX_PHONE_FILLING) {
                    digitsOnly = digitsOnly.substring(0, CleanerFX.MAX_PHONE_FILLING);
                }

                filterPhoneField.setText(digitsOnly);
            }
        });
    }

    /**
     * Заполняет таблицу данными о мойщиках
     */
    private void fillingAll() {
        try{
            List<Cleaner> cleaners = cleanersRepository.get(null,null,null,null,CleanerStatus.ACT);
            fillingObservableList(cleaners);
            cleanersTable.setItems(cleanerFXES);
            cleanersTable.getSelectionModel().selectFirst();
            Platform.runLater(() -> cleanersTable.requestFocus());

            fillingStatusComboBox();
        }catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * Заполняет ObservableList данными о мойщиках
     *
     * @param cleaners список мойщиков для заполнения
     */
    private void fillingObservableList(List<Cleaner> cleaners){
        for(Cleaner cleaner:cleaners){
            CleanerFX cleanerFX = new CleanerFX(cleaner.getClrId(),cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic(), cleaner.getClrPhone(), cleaner.getClrPhotoName(), cleaner.getClrStatus());
            cleanerFXES.add(cleanerFX);
        }
    }


    /**
     * Устанавливает конвертер для комбо-бокса статусов мойщика.
     * Конвертер используется для отображения объектов типа {@code CleanerStatus} в строковом представлении
     * и обратно при необходимости.
     */
    private void setConvertersForComboBoxes(){
        filterStatusComboBox.setConverter(new StringConverter<CleanerStatus>() {
            @Override
            public String toString(CleanerStatus status) {
                return status == null ? null : status.getDisplayValue();
            }
            @Override
            public CleanerStatus fromString(String string) {
                return null;
            }
        });
    }

    /**
     * Заполняет комбо-бокс статусов мойщика значениями и устанавливает выбранный элемент по умолчанию.
     */
    private void fillingStatusComboBox(){
        filterStatusComboBox.getItems().setAll(CleanerStatus.values());
        filterStatusComboBox.getItems().add(0,null);
        filterStatusComboBox.getSelectionModel().select(CleanerStatus.ACT);

    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_CLEANER")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_CLEANER")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_CLEANER")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CLEANER")));
        });
        showPhotoButton.setOnMouseEntered(event->{
            showPhotoButton.setTooltip(new Tooltip(rb.getString("SHOW_PHOTO")));
        });
        getWorkScheduleCleanerButton.setOnMouseEntered(event->{
            getWorkScheduleCleanerButton.setTooltip(new Tooltip(rb.getString("SHOW_WORK_SCHEDULE_CLEANER")));
        });
        showBookingsButton.setOnMouseEntered(event->{
            showBookingsButton.setTooltip(new Tooltip(rb.getString("SHOW_BOOKINGS")));
        });
    }

    /**
     * Возвращает текущую сцену (Scene).
     *
     * @return текущая сцена
     */
    private Scene getActualScene(){
        return cleanersTable.getScene();
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


    /**
     * Выполняет операции с мойщиком
     * @param operationMode Режим операции
     */
    private void doOperation(FXOperationMode operationMode){
        Cleaner cleaner = null;
        CleanerFX selectedCleanerFX = null;
        switch (operationMode){
            case CREATE:
                cleaner = new Cleaner();
                break;
            case EDIT:
            case DELETE:
                selectedCleanerFX = cleanersTable.getSelectionModel().getSelectedItem();
                if(selectedCleanerFX != null){
                    cleaner = new Cleaner();
                    cleaner.setClrId(selectedCleanerFX.getClrId());
                    cleaner.setClrSurname(selectedCleanerFX.getClrSurname());
                    cleaner.setClrName(selectedCleanerFX.getClrName());
                    cleaner.setClrPatronymic(selectedCleanerFX.getClrPatronymic());
                    cleaner.setClrPhone(selectedCleanerFX.getClrPhone());
                    cleaner.setClrPhotoName(selectedCleanerFX.getClrPhotoName());
                    cleaner.setClrStatus(CleanerStatus.valueOfDisplayValue(selectedCleanerFX.getClrStatus()));
                }
                break;
        }
        if(cleaner == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLEANER"));
            cleanersTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Cleaners.resources.EditCleaner", "Cleaners/fxml/EditCleaner.fxml", getActualScene());
                EditCleanerController editCleanerController = fxWindowData.getLoader().getController();

                editCleanerController.setParameters(cleaner, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
                doResult(operationMode, editCleanerController.getExitMode(), cleaner, selectedCleanerFX);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                cleanersTable.requestFocus();
            }
        }
    }

    /**
     * Обрабатывает результат выполнения операции.
     *
     * @param operationMode режим операции
     * @param exitMode режим выхода из формы
     * @param cleaner объект мойщика
     * @param selectedCleanerFX выбранный CleanerFX для изменения или удаления
     */
    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, Cleaner cleaner, CleanerFX selectedCleanerFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    CleanerFX cleanerFX = new CleanerFX(cleaner.getClrId(),cleaner.getClrSurname(), cleaner.getClrName(),cleaner.getClrPatronymic(),cleaner.getClrPhone(),cleaner.getClrPhotoName(),cleaner.getClrStatus());
                    cleanerFXES.add(cleanerFX);
                    cleanerFXES.sort(Comparator.comparing(CleanerFX::getClrStatus, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrSurname, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrName, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPatronymic, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPhone, String::compareToIgnoreCase));

                    cleanersTable.getSelectionModel().select(cleanerFX);
                    statusColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    selectedCleanerFX.setClrSurname(cleaner.getClrSurname());
                    selectedCleanerFX.setClrName(cleaner.getClrName());
                    selectedCleanerFX.setClrPatronymic(cleaner.getClrPatronymic());
                    selectedCleanerFX.setClrPhone(cleaner.getClrPhone());

                    selectedCleanerFX.setClrStatus(cleaner.getClrStatus().getDisplayValue());
                    selectedCleanerFX.setClrPhotoName(cleaner.getClrPhotoName());

                    cleanerFXES.sort(Comparator.comparing(CleanerFX::getClrStatus, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrSurname, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrName, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPatronymic, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPhone, String::compareToIgnoreCase));

                    cleanersTable.getSelectionModel().select(selectedCleanerFX);
                    statusColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    cleanerFXES.remove(selectedCleanerFX);
                    break;
            }
        }
        cleanersTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    /**
     * Выполняет обновление данных.
     */
    private void doRefresh(){
        filterSurnameField.clear();
        filterNameField.clear();
        filterPatronymicField.clear();
        filterPhoneField.clear();
        cleanerFXES.clear();

        fillingAll();
    }
    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            cleanerFXES.clear();

            List<Cleaner> cleaners = cleanersRepository.get(filterSurnameField.getText().trim(), filterNameField.getText().trim(), filterPatronymicField.getText().trim(), filterPhoneField.getText().trim(), filterStatusComboBox.getValue());
            fillingObservableList(cleaners);
            cleanersTable.setItems(cleanerFXES);
            cleanersTable.requestFocus();
            cleanersTable.getSelectionModel().selectFirst();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    public void showPhotoButtonAction(ActionEvent actionEvent) {
        if(cleanersTable.getSelectionModel().getSelectedItem() == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLEANER"));
        }else{
            long clrId = cleanersTable.getSelectionModel().getSelectedItem().getClrId();
            Optional<CleanerFX> cleanerFX = cleanerFXES.stream()
                    .filter(c->c.getClrId() == clrId)
                    .findFirst();

            if(cleanerFX.isPresent()){
                try {
                    CleanerFX existedCleanerFX = cleanerFX.get();

                    Cleaner cleaner = new Cleaner();
                    cleaner.setClrSurname(existedCleanerFX.getClrSurname());
                    cleaner.setClrName(existedCleanerFX.getClrName());
                    cleaner.setClrPatronymic(existedCleanerFX.getClrPatronymic());
                    cleaner.setClrPhotoName(existedCleanerFX.getClrPhotoName());

                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Cleaners.resources.ShowCleanerPhoto", "Cleaners/fxml/ShowCleanerPhoto.fxml", getActualScene());
                    ShowCleanerPhotoController showCleanerPhotoController = fxWindowData.getLoader().getController();
                    showCleanerPhotoController.showPhoto(cleaner);
                    fxWindowData.getModalStage().showAndWait();

                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    cleanersTable.requestFocus();
                }
            }
        }
        cleanersTable.requestFocus();
    }

    public void getWorkScheduleCleanerButtonAction(ActionEvent actionEvent) {
        if(cleanersTable.getSelectionModel().getSelectedItem() == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLEANER"));
        }else{
            CleanerFX cleanerFX = cleanersTable.getSelectionModel().getSelectedItem();
            try {
                Cleaner cleaner = new Cleaner();
                cleaner.setClrId(cleanerFX.getClrId());
                cleaner.setClrSurname(cleanerFX.getClrSurname());
                cleaner.setClrName(cleanerFX.getClrName());
                cleaner.setClrPatronymic(cleanerFX.getClrPatronymic());

                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.WorkSchedule.resources.ViewWorkScheduleCleaner", "WorkSchedule/fxml/ViewWorkScheduleCleaner.fxml", getActualScene());
                ViewWorkScheduleCleanerController viewWorkScheduleCleanerController = fxWindowData.getLoader().getController();
                viewWorkScheduleCleanerController.settingForm(cleaner,fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }

        cleanersTable.requestFocus();
    }

    public void showBookingsButtonAction(ActionEvent actionEvent) {
        try{
            CleanerFX cleanerFX = cleanersTable.getSelectionModel().getSelectedItem();
            if(cleanerFX == null){
                FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLEANER"));
            }else{
                Cleaner cleaner = new Cleaner();
                cleaner.setClrId(cleanerFX.getClrId());
                cleaner.setClrSurname(cleanerFX.getClrSurname());
                cleaner.setClrName(cleanerFX.getClrName());
                cleaner.setClrPatronymic(cleanerFX.getClrPatronymic());

                FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.FilterBookings", "Bookings/fxml/FilterBookings.fxml");
                FilterBookingsController filterBookingsController = fxWindowData.getLoader().getController();
                filterBookingsController.setParameters(null,cleaner,fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            cleanersTable.requestFocus();
        }

        cleanersTable.requestFocus();
    }
}
