package ru.pin120.carwashemployee.Cleaners;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.util.StringConverter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransportFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.*;

public class CleanersController implements Initializable {

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
    private ComboBox<Box> filterBoxNumberComboBox;
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
    private TableColumn<CleanerFX,Long> boxNumberColumn;
    @FXML
    private TableColumn<CleanerFX,String> phoneColumn;
    @FXML
    private TableColumn<CleanerFX,Long> idColumn;
    private ResourceBundle rb;
    private CleanersRepository cleanersRepository = new CleanersRepository();
    private BoxesRepository boxesRepository = new BoxesRepository();
    private ObservableList<CleanerFX> cleanerFXES = FXCollections.observableArrayList();
    private List<Box> boxes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        surnameColumn.setCellValueFactory(c -> c.getValue().clrSurnameProperty());
        nameColumn.setCellValueFactory(c -> c.getValue().clrNameProperty());
        patronymicColumn.setCellValueFactory(c -> c.getValue().clrPatronymicProperty());
        phoneColumn.setCellValueFactory(c -> c.getValue().clrPhoneProperty());
        statusColumn.setCellValueFactory(c -> c.getValue().clrStatusProperty());
        idColumn.setCellValueFactory(c -> c.getValue().clrIdProperty().asObject());
        boxNumberColumn.setCellValueFactory(c -> {
            CleanerFX cleanerFX = c.getValue();
            return cleanerFX.getBoxId() == null ? null : cleanerFX.boxIdProperty().asObject();
        });

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

    private void fillingAll() {
        try{
            List<Cleaner> cleaners = cleanersRepository.get(null,null,null,null,CleanerStatus.WORKING, null);
            fillingObservableList(cleaners);
            cleanersTable.setItems(cleanerFXES);
            cleanersTable.getSelectionModel().selectFirst();
            Platform.runLater(() -> cleanersTable.requestFocus());

            fillingStatusComboBox();
            fillingBoxNumberComboBox();
        }catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<Cleaner> cleaners){
        for(Cleaner cleaner:cleaners){
            CleanerFX cleanerFX = new CleanerFX(cleaner.getClrId(),cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic(), cleaner.getClrPhone(), cleaner.getClrPhotoName(), cleaner.getClrStatus(),  cleaner.getBox().getBoxId());
            cleanerFXES.add(cleanerFX);
        }
    }


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

        filterBoxNumberComboBox.setConverter(new StringConverter<Box>() {
            @Override
            public String toString(Box box) {
                return box == null ? null : box.getBoxId() == null ? "" : box.getBoxId().toString();
            }
            @Override
            public Box fromString(String string) {
                if (string == null || string.isBlank()) {
                    return null;
                }
                try {
                    Long id = Long.parseLong(string);
                    return boxes.stream()
                            .filter(box -> box.getBoxId().equals(id))
                            .findFirst()
                            .orElse(null);
                } catch (NumberFormatException e) {
                    return null;
                }
            }
        });
    }
    private void fillingStatusComboBox(){
        filterStatusComboBox.getItems().setAll(CleanerStatus.values());
        filterStatusComboBox.getItems().add(0,null);
        filterStatusComboBox.getSelectionModel().select(CleanerStatus.WORKING);

    }
    private void fillingBoxNumberComboBox() throws Exception{
        filterBoxNumberComboBox.getItems().clear();

        boxes = boxesRepository.getAll();
        List<Box> check = new ArrayList<>();
        check.add(new Box());
        check.addAll(boxes);
        //filterBoxNumberComboBox.getItems().add(null);
        filterBoxNumberComboBox.getItems().addAll(check);
        //Platform.runLater(()->filterBoxNumberComboBox.getSelectionModel().select(0));

    }

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
    }

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

    private void doOperation(FXOperationMode operationMode){
        Cleaner cleaner = null;
        CleanerFX selectedCleanerFX = null;
        switch (operationMode){
            case CREATE:
                cleaner = new Cleaner();
                break;
            case EDIT:
            case DELETE:
                if(cleanersTable.getSelectionModel().getSelectedItem() != null){
                    selectedCleanerFX = cleanersTable.getSelectionModel().getSelectedItem();
                    cleaner = new Cleaner();
                    cleaner.setClrId(selectedCleanerFX.getClrId());
                    cleaner.setClrSurname(selectedCleanerFX.getClrSurname());
                    cleaner.setClrName(selectedCleanerFX.getClrName());
                    cleaner.setClrPatronymic(selectedCleanerFX.getClrPatronymic());
                    cleaner.setClrPhone(selectedCleanerFX.getClrPhone());
                    cleaner.setClrPhotoName(selectedCleanerFX.getClrPhotoName());
                    cleaner.setClrStatus(CleanerStatus.valueOfDisplayValue(selectedCleanerFX.getClrStatus()));

                    long selectedCleanerFXBoxId = selectedCleanerFX.getBoxId();
                    Box box = boxes.stream()
                            .filter(b -> b.getBoxId() == selectedCleanerFXBoxId)
                            .findFirst()
                            .orElse(null);
                    cleaner.setBox(box);

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

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, Cleaner cleaner, CleanerFX selectedCleanerFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    CleanerFX cleanerFX = new CleanerFX(cleaner.getClrId(),cleaner.getClrSurname(), cleaner.getClrName(),cleaner.getClrPatronymic(),cleaner.getClrPhone(),cleaner.getClrPhotoName(),cleaner.getClrStatus(),cleaner.getBox().getBoxId());
                    cleanerFXES.add(cleanerFX);
                    cleanerFXES.sort(Comparator.comparing(CleanerFX::getClrStatus, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrSurname, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrName, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPatronymic, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPhone, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getBoxId));

                    cleanersTable.getSelectionModel().select(cleanerFX);
                    statusColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    selectedCleanerFX.setClrSurname(cleaner.getClrSurname());
                    selectedCleanerFX.setClrName(cleaner.getClrName());
                    selectedCleanerFX.setClrPatronymic(cleaner.getClrPatronymic());
                    selectedCleanerFX.setClrPhone(cleaner.getClrPhone());
                    selectedCleanerFX.setBoxId(cleaner.getBox().getBoxId());
                    selectedCleanerFX.setClrStatus(cleaner.getClrStatus().getDisplayValue());
                    selectedCleanerFX.setClrPhotoName(cleaner.getClrPhotoName());

                    cleanerFXES.sort(Comparator.comparing(CleanerFX::getClrStatus, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrSurname, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrName, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPatronymic, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getClrPhone, String::compareToIgnoreCase)
                            .thenComparing(CleanerFX::getBoxId));

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

            List<Cleaner> cleaners = cleanersRepository.get(filterSurnameField.getText().trim(), filterNameField.getText().trim(), filterPatronymicField.getText().trim(), filterPhoneField.getText().trim(), filterStatusComboBox.getValue(), filterBoxNumberComboBox.getValue() == null ? null : filterBoxNumberComboBox.getValue().getBoxId());
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
}
