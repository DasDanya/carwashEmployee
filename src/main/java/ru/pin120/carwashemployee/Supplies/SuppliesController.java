package ru.pin120.carwashemployee.Supplies;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.*;

/**
 * Контроллер формы с расходными материалами
 */
public class SuppliesController implements Initializable {
    @FXML
    private Button addSupplyInBoxButton;
    @FXML
    private Button showPhotoButton;
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
    private TableView<SupplyFX> suppliesTable;
    @FXML
    private TableColumn<SupplyFX, String> nameColumn;
    @FXML
    private TableColumn<SupplyFX, String> categoryColumn;
    @FXML
    private TableColumn<SupplyFX, Integer> countColumn;
    @FXML
    private TableColumn<SupplyFX, String> measureColumn;
    @FXML
    private TableColumn<SupplyFX, Long> idColumn;
    @FXML
    private Pagination pagination;
    private ResourceBundle rb;

    List<Supply> supplies = new ArrayList<>();
    ObservableList<SupplyFX> supplyFXES = FXCollections.observableArrayList();
    private SupplyRepository supplyRepository = new SupplyRepository();

    private String filterName = "";
    private String filterCategory = "";
    private String filterOperator = "";
    private Integer filterCount = null;

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(s->s.getValue().supIdProperty().asObject());
        nameColumn.setCellValueFactory(s->s.getValue().supNameProperty());
        categoryColumn.setCellValueFactory(s->s.getValue().supCategoryProperty());
        measureColumn.setCellValueFactory(s->s.getValue().supMeasureProperty());
        countColumn.setCellValueFactory(s->s.getValue().supCountProperty().asObject());
        suppliesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FXHelper.setContextMenuForEditableTextField(filterCategoryField);
        FXHelper.setContextMenuForEditableTextField(filterNameField);

        List<String> operators = new ArrayList<>(Arrays.asList(" ","<", ">", "=", "<=", ">=", "!="));
        operationCountComboBox.getItems().setAll(operators);

        fillingTable(0);
        setTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
        pageIndexListener();

        try {
            settingCountSpinner();
        }catch (Exception e){

        }
    }

    /**
     * Заполняет таблицу расходных материалов (suppliesTable), исходя из текущего индекса страницы.
     *
     * @param pageIndex индекс текущей страницы пагинации
     */
    private void fillingTable(int pageIndex) {
        try{
            supplies.clear();
            supplyFXES.clear();
            if(filterName.isBlank() && filterCategory.isBlank() && filterOperator.isBlank() &&  filterCount == null){
                supplies = supplyRepository.get(pageIndex);
            }else{
                supplies = supplyRepository.get(pageIndex,filterName,filterCategory,filterOperator,filterCount);
            }
            fillingObservableList();

            suppliesTable.setItems(supplyFXES);
            suppliesTable.getSelectionModel().selectFirst();
            Platform.runLater(()->suppliesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            suppliesTable.requestFocus();
        }
    }

    /**
     * Заполняет ObservableList данными о расходных материалах
     */
    private void fillingObservableList() {
        for(Supply supply: supplies){
            String measure = supply.getSupMeasure() + " " + supply.getCategory().getUnit().getDisplayValue();
            SupplyFX supplyFX = new SupplyFX(supply.getSupId(), supply.getSupName(), supply.getCategory().getCsupName(), measure, supply.getSupPhotoName(),supply.getSupCount());
            supplyFXES.add(supplyFX);
        }
    }

    /**
     * Настраивает filterCountSpinner с соответствующей фабрикой значений и текстовым форматтером.
     */
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

    /**
     * Устанавливает слушателя изменения текущей страницы пагинации (pagination).
     * При изменении текущей страницы вызывает метод fillingTable для загрузки данных новой страницы.
     */
    private void pageIndexListener() {
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    /**
     * Возвращает текущую сцену (Scene).
     *
     * @return текущая сцена
     */
    private Scene getActualScene(){
        return suppliesTable.getScene();
    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
    private void setTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_SUPPLY")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_SUPPLY")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_SUPPLY")));
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
        addSupplyInBoxButton.setOnMouseEntered(event->{
            addSupplyInBoxButton.setTooltip(new Tooltip(rb.getString("ADD_SUPPLY_IN_BOX")));
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

    /**
     * Выполняет операции с расходным материалом
     * @param operationMode Режим операции
     */
    private void doOperation(FXOperationMode operationMode){
        Supply supply = null;
        switch (operationMode){
            case CREATE:
                supply = new Supply();
                break;
            case EDIT:
            case DELETE:
                SupplyFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
                if(selectedSupplyFX != null){
                    supply = supplies.stream()
                            .filter(s->s.getSupId().longValue() == selectedSupplyFX.getSupId())
                            .findFirst()
                            .orElse(null);
                }
                break;
        }
        if(supply == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
            suppliesTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Supplies.resources.EditSupply", "Supplies/fxml/EditSupply.fxml", getActualScene());
                EditSupplyController editSupplyController = fxWindowData.getLoader().getController();

                editSupplyController.setParameters(supply, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

                doResult(editSupplyController.getExitMode(), operationMode, supply);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                suppliesTable.requestFocus();
            }
        }
    }

    /**
     * Обрабатывает результат выполнения операции.
     *
     * @param operationMode режим операции
     * @param exitMode режим выхода из формы
     * @param supply объект расходного материала
     */
    private void doResult(FXFormExitMode exitMode, FXOperationMode operationMode, Supply supply) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    int indexPage = supplyFXES.isEmpty() ? 0 : pagination.getCurrentPageIndex();
                    fillingTable(indexPage);
                    Optional<SupplyFX> supplyFX = supplyFXES.stream()
                            .filter(s->s.getSupId() == supply.getSupId())
                            .findFirst();

                    supplyFX.ifPresent(fx -> suppliesTable.getSelectionModel().select(fx));
                    categoryColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    fillingTable(pagination.getCurrentPageIndex());
                    Optional<SupplyFX> supplyFXed = supplyFXES.stream()
                            .filter(s->s.getSupId() == supply.getSupId())
                            .findFirst();

                    supplyFXed.ifPresent(fx -> suppliesTable.getSelectionModel().select(fx));
                    categoryColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    fillingTable(pagination.getCurrentPageIndex());
                    break;
            }
        }

        suppliesTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    /**
     * Обновление данных
     */
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
        SupplyFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
        if(selectedSupplyFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
        }else{
            try {
                Supply supply = supplies.stream()
                        .filter(s->s.getSupId().longValue() == selectedSupplyFX.getSupId())
                        .findFirst()
                        .orElse(null);

                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Supplies.resources.ShowSupplyPhoto", "Supplies/fxml/ShowSupplyPhoto.fxml", getActualScene());
                ShowSupplyPhotoController showSupplyPhotoController = fxWindowData.getLoader().getController();
                showSupplyPhotoController.showPhoto(supply);
                fxWindowData.getModalStage().showAndWait();

            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
                suppliesTable.requestFocus();
            }
        }
        suppliesTable.requestFocus();
    }

    public void addSupplyInBoxButtonAction(ActionEvent actionEvent) {
        SupplyFX selectedSupplyFX = suppliesTable.getSelectionModel().getSelectedItem();
        if(selectedSupplyFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_SUPPLY"));
        }else{
            try {

                Supply supply = supplies.stream()
                        .filter(s->s.getSupId().longValue() == selectedSupplyFX.getSupId())
                        .findFirst()
                        .orElse(null);

                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Supplies.resources.AddSupplyInBox", "Supplies/fxml/AddSupplyInBox.fxml", getActualScene());
                AddSupplyInBoxController addSupplyInBoxController = fxWindowData.getLoader().getController();
                addSupplyInBoxController.setParameters(supply,fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

                if(addSupplyInBoxController.getExitMode() == FXFormExitMode.OK){
                    fillingTable(pagination.getCurrentPageIndex());
                    Optional<SupplyFX> supplyFXed = supplyFXES.stream()
                            .filter(s->s.getSupId() == supply.getSupId())
                            .findFirst();

                    supplyFXed.ifPresent(fx -> suppliesTable.getSelectionModel().select(fx));
                }

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                suppliesTable.requestFocus();
            }
        }
        
        suppliesTable.requestFocus();
    }
}
