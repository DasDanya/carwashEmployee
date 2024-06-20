package ru.pin120.carwashemployee.SuppliesInBox;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Supplies.ShowSupplyPhotoController;
import ru.pin120.carwashemployee.Supplies.Supply;
import ru.pin120.carwashemployee.Supplies.SupplyFX;


import java.net.URL;
import java.util.*;

/**
 * Контроллер формы с расходными материалами в боксе
 */
public class SuppliesInBoxController implements Initializable {

    private Long boxId;
    @FXML
    public Button showPhotoButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button addInWarehouseButton;
    @FXML
    private Button decreaseButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button showFilterButton;
    @FXML
    private TextField filterNameField;
    @FXML
    private TextField filterCategoryField;
    @FXML
    private ComboBox<String> operationCountComboBox;
    @FXML
    private Spinner<Integer> filterCountSpinner;
    @FXML
    private TableView<SuppliesInBoxFX> suppliesTable;
    @FXML
    private TableColumn<SuppliesInBoxFX, String> nameColumn;
    @FXML
    private TableColumn<SuppliesInBoxFX, String> categoryColumn;
    @FXML
    private TableColumn<SuppliesInBoxFX, String> measureColumn;
    @FXML
    private TableColumn<SuppliesInBoxFX, Integer> countColumn;
    @FXML
    private TableColumn<SuppliesInBoxFX, Long> idColumn;
    @FXML
    private Pagination pagination;
    private ResourceBundle rb;
    private ObservableList<SuppliesInBoxFX> suppliesInBoxFXES = FXCollections.observableArrayList();
    private List<SuppliesInBox> suppliesInBox = new ArrayList<>();
    private SuppliesInBoxRepository suppliesInBoxRepository = new SuppliesInBoxRepository();

    private String filterName = "";
    private String filterCategory = "";
    private String filterOperator = "";
    private Integer filterCount = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(s->s.getValue().sibIdProperty().asObject());
        nameColumn.setCellValueFactory(s->s.getValue().supNameProperty());
        categoryColumn.setCellValueFactory(s->s.getValue().supCategoryProperty());
        measureColumn.setCellValueFactory(s->s.getValue().supMeasureProperty());
        countColumn.setCellValueFactory(s->s.getValue().supCountProperty().asObject());
        suppliesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FXHelper.setContextMenuForEditableTextField(filterCategoryField);
        FXHelper.setContextMenuForEditableTextField(filterNameField);

        List<String> operators = new ArrayList<>(Arrays.asList(" ","<", ">", "=", "<=", ">=", "!="));
        operationCountComboBox.getItems().setAll(operators);

        setTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
        pageIndexListener();

        try {
            settingCountSpinner();
        }catch (Exception e){

        }
    }

    private void setTooltipForButtons() {
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_SUPPLY")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_SUPPLY")));
        });
        showPhotoButton.setOnMouseEntered(event->{
            showPhotoButton.setTooltip(new Tooltip(rb.getString("SHOW_PHOTO")));
        });
        showFilterButton.setOnMouseEntered(event->{
            showFilterButton.setTooltip(new Tooltip(rb.getString("SHOW_LAST_FILTER")));
        });
        addInWarehouseButton.setOnMouseEntered(event->{
            addInWarehouseButton.setTooltip(new Tooltip(rb.getString("ADD_SUPPLY_IN_WAREHOUSE")));
        });
        decreaseButton.setOnMouseEntered(event->{
            decreaseButton.setTooltip(new Tooltip(rb.getString("DECREASE")));
        });
    }

    private Scene getActualScene(){
        return suppliesTable.getScene();
    }

    private void settingCountSpinner() {
        filterCountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE,0,1));
        FXHelper.setContextMenuForEditableTextField(filterCountSpinner.getEditor());
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                return change;
            }
            return null;
        });

        filterCountSpinner.getEditor().setTextFormatter(formatter);
    }

    public void startFilling(Long boxId, Stage stage){
        this.boxId = boxId;
        fillingTable(0);
        stage.setTitle(String.format(rb.getString("FORM_TITLE"),boxId));
    }
    private void pageIndexListener() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    private void fillingTable(int pageIndex){
        try{
            suppliesInBox.clear();
            suppliesInBoxFXES.clear();

            suppliesInBox = suppliesInBoxRepository.get(boxId,pageIndex,filterName,filterCategory,filterOperator,filterCount);
            fillingObservableList();

            suppliesTable.setItems(suppliesInBoxFXES);
            suppliesTable.getSelectionModel().selectFirst();
            Platform.runLater(()->suppliesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            suppliesTable.requestFocus();
        }
    }

    private void fillingObservableList() {
        for(SuppliesInBox s: suppliesInBox){
            String measure = s.getSupply().getSupMeasure() + " " + s.getSupply().getCategory().getUnit().getDisplayValue();
            SuppliesInBoxFX suppliesInBoxFX = new SuppliesInBoxFX(s.getSibId(), s.getSupply().getSupName(), s.getSupply().getCategory().getCsupName(), measure, s.getSupply().getSupPhotoName(),s.getCountSupplies());
            suppliesInBoxFXES.add(suppliesInBoxFX);
        }
    }

    private void doOperation(FXOperationMode operationMode) {
        if (operationMode == FXOperationMode.DELETE) {
            SuppliesInBoxFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
            if (selectedSupplyFX == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
            } else {
                try {
                    SuppliesInBox supplyInBox = getSupplyInBox(selectedSupplyFX);
                    if (supplyInBox != null) {
                        Optional<ButtonType> result = FXHelper.createConfirmAlert(rb.getString("CONFIRM_DELETE_TITLE"), String.format(rb.getString("CONFIRM_DELETE_TEXT"), supplyInBox.getSupply().getCategory().getCsupName(), supplyInBox.getSupply().getSupName(), supplyInBox.getSupply().getSupMeasure(), supplyInBox.getSupply().getCategory().getUnit().getDisplayValue()));
                        if (result.isPresent() && result.get() == ButtonType.OK) {
                            if (suppliesInBoxRepository.delete(supplyInBox.getSibId())) {
                                fillingTable(pagination.getCurrentPageIndex());
                            }
                        }
                    } else {
                        FXHelper.showErrorAlert(rb.getString("SUPPLY_NOT_EXIST"));
                    }
                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    suppliesTable.requestFocus();
                }
            }
        }

        suppliesTable.requestFocus();
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
       doOperation(FXOperationMode.DELETE);
    }

    private SuppliesInBox getSupplyInBox(SuppliesInBoxFX suppliesInBoxFX){
        return suppliesInBox.stream()
                .filter(s -> s.getSibId() == suppliesInBoxFX.getSibId())
                .findFirst()
                .orElse(null);
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        filterNameField.clear();
        filterCategoryField.clear();
        filterCountSpinner.getValueFactory().setValue(0);
        operationCountComboBox.getSelectionModel().selectFirst();

        filterName = "";
        filterCategory = "";
        filterOperator = "";
        filterCount = null;

        fillingTable(0);
        pagination.setCurrentPageIndex(0);
    }

    public void addInWarehouseButtonAction(ActionEvent actionEvent) {
        showDecreaseWindow(DecreaseSuppliesInBoxStatus.TRANSFER);
    }

    public void decreaseButtonAction(ActionEvent actionEvent) {
        showDecreaseWindow(DecreaseSuppliesInBoxStatus.DECREASE);
    }

    private void showDecreaseWindow(DecreaseSuppliesInBoxStatus status){
        SuppliesInBoxFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
        if(selectedSupplyFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
        }else{
            try {
                SuppliesInBox supplyInBox = getSupplyInBox(selectedSupplyFX);
                if(supplyInBox != null) {
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.SuppliesInBox.resources.DecreaseSuppliesInBox", "SuppliesInBox/fxml/DecreaseSuppliesInBox.fxml", getActualScene());
                    DecreaseSuppliesInBoxController decreaseSuppliesInBoxController = fxWindowData.getLoader().getController();
                    decreaseSuppliesInBoxController.setParameters(supplyInBox, status, fxWindowData.getModalStage());
                    fxWindowData.getModalStage().showAndWait();

                    doResultDecreaseFromBox(decreaseSuppliesInBoxController.getExitMode(), supplyInBox);
                }else{
                    FXHelper.showErrorAlert(rb.getString("SUPPLY_NOT_EXIST"));
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                suppliesTable.requestFocus();
            }
        }

        suppliesTable.requestFocus();
    }

    private void doResultDecreaseFromBox(FXFormExitMode exitMode, SuppliesInBox suppliesInBox){
        if(exitMode == FXFormExitMode.OK){
            fillingTable(pagination.getCurrentPageIndex());
            Optional<SuppliesInBoxFX> supplyFXed = suppliesInBoxFXES.stream()
                    .filter(s->s.getSibId() == suppliesInBox.getSibId())
                    .findFirst();

            supplyFXed.ifPresent(fx -> suppliesTable.getSelectionModel().select(fx));
        }
        suppliesTable.requestFocus();
    }

    public void showFilterButtonAction(ActionEvent actionEvent) {
        String message = String.format("%s:%s\n%s:%s\n%s:%s\n%s:%s",rb.getString("NAME"), filterName, rb.getString("CATEGORY"),filterCategory, rb.getString("COMPARING_OPERATOR"),filterOperator,
                rb.getString("COUNT"), filterCount == null ? "" : filterCount);
        FXHelper.showInfoAlert(message);
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            filterCategory = filterCategoryField.getText().trim();
            filterName = filterNameField.getText().trim();

            //if(operationDiscountComboBox.getSelectionModel().getSelectedItem() != null && !operationDiscountComboBox.getSelectionModel().getSelectedItem().isBlank()) {
            if(operationCountComboBox.getSelectionModel().getSelectedItem() != null) {
                filterCount = filterCountSpinner.getValue();
                filterOperator = operationCountComboBox.getSelectionModel().getSelectedItem();
            }

            fillingTable(0);
            pagination.setCurrentPageIndex(0);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            suppliesTable.requestFocus();
        }
    }

    public void showPhotoButtonAction(ActionEvent actionEvent) {
        SuppliesInBoxFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
        if(selectedSupplyFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
        }else{
            try {
                SuppliesInBox supplyInBox = getSupplyInBox(selectedSupplyFX);

                if(supplyInBox != null) {
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Supplies.resources.ShowSupplyPhoto", "Supplies/fxml/ShowSupplyPhoto.fxml", getActualScene());
                    ShowSupplyPhotoController showSupplyPhotoController = fxWindowData.getLoader().getController();
                    showSupplyPhotoController.showPhoto(supplyInBox.getSupply());
                    fxWindowData.getModalStage().showAndWait();
                }else{
                    FXHelper.showErrorAlert(rb.getString("SUPPLY_NOT_EXIST"));
                }

            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
                suppliesTable.requestFocus();
            }
        }
        suppliesTable.requestFocus();
    }

}
