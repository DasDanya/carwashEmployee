package ru.pin120.carwashemployee.PriceListPosition;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.*;

public class PriceListPositionController implements Initializable {

    @FXML
    private TextField filterCategoryField;
    @FXML
    private Spinner<Integer> filterPriceSpinner;
    @FXML
    private ComboBox<String> operationPriceComboBox;
    @FXML
    private Spinner<Integer> filterTimeSpinner;
    @FXML
    private ComboBox<String> operationTimeComboBox;
    @FXML
    private Stage stage;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button createButton;
    @FXML
    private Button searchButton;
    @Getter
    private FXFormExitMode exitMode;
    @FXML
    private TableView<PriceListPositionFX> priceListTable;

    @FXML
    private TableColumn<PriceListPositionFX, String> categoryTransportColumn;
    @FXML
    private TableColumn<PriceListPositionFX, Integer> priceColumn;
    @FXML
    private TableColumn<PriceListPositionFX, Integer> timeColumn;
    @FXML
    private TableColumn<PriceListPositionFX, Long> idColumn;


    private ResourceBundle rb;

    private String serviceName;

    private List<PriceListPosition> prices;
    private ObservableList<PriceListPositionFX> priceListPositionFXES = FXCollections.observableArrayList();

    private PriceListPositionRepository priceListPositionRepository = new PriceListPositionRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryTransportColumn.setCellValueFactory(st->st.getValue().catTrNameProperty());
        priceColumn.setCellValueFactory(st->st.getValue().priceProperty().asObject());
        timeColumn.setCellValueFactory(st->st.getValue().timeProperty().asObject());
        idColumn.setCellValueFactory(st->st.getValue().idProperty().asObject());

        initFilterPanel();
        setTooltipForButton();
        Platform.runLater(this::fillingAll);
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));


    }

    private void initFilterPanel(){
        setSpinnersFormatters();

        List<String> operators = new ArrayList<>(Arrays.asList(" ","<", ">", "="));
        operationPriceComboBox.getItems().setAll(operators);
        operationTimeComboBox.getItems().setAll(operators);

        FXHelper.setContextMenuForTextField(filterTimeSpinner.getEditor());
        FXHelper.setContextMenuForTextField(filterPriceSpinner.getEditor());
        FXHelper.setContextMenuForTextField(filterCategoryField);
    }

    private void setSpinnersFormatters(){
        filterPriceSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,PriceListPositionFX.MAX_PRICE,0,50));
        filterTimeSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,PriceListPositionFX.MAX_TIME,1,1));

        TextFormatter<Integer> priceFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= PriceListPositionFX.MAX_PRICE) {
                    return change;
                }
            }
            return null;
        });

        filterPriceSpinner.getEditor().setTextFormatter(priceFormatter);

        TextFormatter<Integer> timeFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= PriceListPositionFX.MAX_TIME) {
                    return change;
                }
            }
            return null;
        });

        filterTimeSpinner.getEditor().setTextFormatter(timeFormatter);
    }

    private void fillingAll(){
        try{
            List<PriceListPosition> priceListPositions = priceListPositionRepository.getByServName(serviceName);
            fillingObservableList(priceListPositions);
            priceListTable.setItems(priceListPositionFXES);
            priceListTable.requestFocus();
            priceListTable.getSelectionModel().selectFirst();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<PriceListPosition> priceListPositions){
        for(PriceListPosition priceListPosition : priceListPositions){
            PriceListPositionFX priceListPositionFX = new PriceListPositionFX(priceListPosition.getCategoryOfTransport().getCatTrName(), priceListPosition.getPlPrice(), priceListPosition.getPlTime(), priceListPosition.getPlId());
            priceListPositionFXES.add(priceListPositionFX);
        }
    }

    public void setParameters(String serviceName, Stage modalStage) {
        this.serviceName = serviceName;
        stage = modalStage;

        stage.setTitle(rb.getString("FORM_TITLE")  + this.serviceName);
        closeWindowAction();

    }

    private void setTooltipForButton(){
        createButton.setOnMouseEntered(event->{
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_RECORD")));
        });
        editButton.setOnMouseEntered(event->{
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_RECORD")));
        });
        deleteButton.setOnMouseEntered(event->{
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_RECORD")));
        });
        refreshButton.setOnMouseEntered(event->{
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
    }

    private Scene getActualScene(){
        return priceListTable.getScene();
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
        PriceListPosition priceListPosition = null;
        PriceListPositionFX selectedPriceListPositionFX = null;

        switch (operationMode){
            case CREATE:
                priceListPosition = new PriceListPosition();
                priceListPosition.setService(new Service(serviceName));
                break;
            case EDIT:
            case DELETE:
                if(priceListTable.getSelectionModel().getSelectedItem() != null){
                    selectedPriceListPositionFX = priceListTable.getSelectionModel().getSelectedItem();

                    priceListPosition = new PriceListPosition();
                    priceListPosition.setService(new Service(serviceName));
                    priceListPosition.setPlPrice(selectedPriceListPositionFX.getPrice());
                    priceListPosition.setPlTime(selectedPriceListPositionFX.getTime());
                    priceListPosition.setPlId(selectedPriceListPositionFX.getId());

                    CategoryOfTransport categoryOfTransport = new CategoryOfTransport();
                    categoryOfTransport.setCatTrName(selectedPriceListPositionFX.getCatTrName());
                    priceListPosition.setCategoryOfTransport(categoryOfTransport);
                }
                break;
        }
        if(priceListPosition == null){
            FXHelper.showErrorAlert("NOT_SELECT_RECORD");
            priceListTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.PriceListPosition.resources.EditPriceListPosition", "PriceListPosition/fxml/EditPriceListPosition.fxml", getActualScene());
                EditPriceListPositionController editPriceListPositionController = fxWindowData.getLoader().getController();

                editPriceListPositionController.setParameters(priceListPosition, operationMode, fxWindowData.getModalStage());

                fxWindowData.getModalStage().showAndWait();
                doResult(operationMode,editPriceListPositionController.getExitMode(), priceListPosition, selectedPriceListPositionFX);
                //doRefresh();

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                priceListTable.requestFocus();
            }
        }
    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, PriceListPosition priceListPosition, PriceListPositionFX selectedPriceListPositionFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    PriceListPositionFX createdPriceListPositionFX = new PriceListPositionFX(priceListPosition.getCategoryOfTransport().getCatTrName(), priceListPosition.getPlPrice(), priceListPosition.getPlTime(), priceListPosition.getPlId());
                    System.out.println(priceListPosition);
                    priceListPositionFXES.add(createdPriceListPositionFX);
                    priceListPositionFXES.sort(Comparator.comparing(PriceListPositionFX::getCatTrName, String::compareToIgnoreCase));

                    //priceListTable.setItems(priceListPositionFXES);
                    priceListTable.getSelectionModel().select(createdPriceListPositionFX);
                    categoryTransportColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    selectedPriceListPositionFX.setTime(priceListPosition.getPlTime());
                    selectedPriceListPositionFX.setPrice(priceListPosition.getPlPrice());

                    //priceListPositionFXES.sort(Comparator.comparing(PriceListPositionFX::getCatTrName, String::compareToIgnoreCase));
                    //priceListTable.getSelectionModel().select(selectedPriceListPositionFX);
                    //categoryTransportColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    priceListPositionFXES.remove(selectedPriceListPositionFX);
                    break;

            }
        }

        priceListTable.requestFocus();
    }

    private void doRefresh(){
        priceListPositionFXES.clear();
        
        filterCategoryField.clear();
        filterPriceSpinner.getValueFactory().setValue(0);
        filterTimeSpinner.getValueFactory().setValue(1);
        operationPriceComboBox.getSelectionModel().selectFirst();
        operationTimeComboBox.getSelectionModel().selectFirst();

        fillingAll();

        priceListTable.getSelectionModel().selectFirst();
        priceListTable.requestFocus();

        categoryTransportColumn.setSortType(TableColumn.SortType.ASCENDING);
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        doSearch();
    }

    private void doSearch(){
        try {
            if((filterCategoryField.getText() != null && !filterCategoryField.getText().isBlank()) || (operationTimeComboBox.getSelectionModel().getSelectedItem() != null && !operationTimeComboBox.getSelectionModel().getSelectedItem().isBlank()) || (operationPriceComboBox.getSelectionModel().getSelectedItem() != null && !operationPriceComboBox.getSelectionModel().getSelectedItem().isBlank())) {
               List<PriceListPosition> priceListPositions = priceListPositionRepository.searchPriceListPosition(serviceName, filterCategoryField.getText().trim(), operationPriceComboBox.getSelectionModel().getSelectedItem(), filterPriceSpinner.getValue(), operationTimeComboBox.getSelectionModel().getSelectedItem(), filterTimeSpinner.getValue());

               priceListPositionFXES.clear();
               fillingObservableList(priceListPositions);

               if(!priceListPositionFXES.isEmpty()){
                   priceListTable.requestFocus();
                   priceListTable.getSelectionModel().selectFirst();
               }
            }else{
                priceListTable.requestFocus();
            }
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
            priceListTable.requestFocus();
        }
    }
}
