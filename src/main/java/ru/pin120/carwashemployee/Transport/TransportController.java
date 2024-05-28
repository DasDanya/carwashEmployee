package ru.pin120.carwashemployee.Transport;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;


import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class TransportController implements Initializable {


    @FXML
    private Pagination pagination;
    @FXML
    private TableColumn<TransportFX, Long> idColumn;
    @FXML
    private TableColumn<TransportFX,String> modelColumn;
    @FXML
    private TableColumn<TransportFX,String> markColumn;
    @FXML
    private TableColumn<TransportFX,String> categoryColumn;
    @FXML
    private TableView<TransportFX> transportsTable;
    @FXML
    private TextField filterModelField;
    @FXML
    private TextField filterMarkField;
    @FXML
    private TextField filterCategoryField;
    @FXML
    private Button showFilterButton;
    @FXML
    private Button searchButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button createButton;
    private ResourceBundle rb;

    String filterCategory = "";
    String filterModel = "";
    String filterMark = "";

    private ObservableList<TransportFX> transportFXES = FXCollections.observableArrayList();

    private TransportRepository transportRepository = new TransportRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(t->t.getValue().trIdProperty().asObject());
        markColumn.setCellValueFactory(t->t.getValue().trMarkProperty());
        modelColumn.setCellValueFactory(t->t.getValue().trModelProperty());
        categoryColumn.setCellValueFactory(t->t.getValue().trCategoryProperty());
        transportsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(filterModelField);
        FXHelper.setContextMenuForEditableTextField(filterCategoryField);

        setTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));

        fillingTable(0);
        pageIndexListener();

    }

//    private void fillingTable(int pageIndex){
//        try{
//            transportFXES.clear();
//            List<Transport> transports = transportRepository.getByPage(pageIndex);
//            fillingObservableList(transports);
//            transportsTable.setItems(transportFXES);
//            transportsTable.getSelectionModel().selectFirst();
//            Platform.runLater(()->transportsTable.requestFocus());
//        }catch (Exception e){
//            FXHelper.showErrorAlert(e.getMessage());
//            transportsTable.requestFocus();
//        }
//    }
    private void fillingTable(int pageIndex){
        try{
            transportFXES.clear();
            List<Transport> transports;
            if(filterCategory.isBlank() && filterMark.isBlank() && filterModel.isBlank()){
             transports = transportRepository.getByPage(pageIndex);
            }else{
                transports = transportRepository.search(pageIndex, filterCategory,filterMark,filterModel);
            }
            fillingObservableList(transports);
            transportsTable.setItems(transportFXES);
            transportsTable.getSelectionModel().selectFirst();
            Platform.runLater(()->transportsTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            transportsTable.requestFocus();
        }
    }

    private void pageIndexListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    private void fillingObservableList(List<Transport> transports){
        for(Transport transport: transports){
            System.out.println(transport.toString() + "\n");
            TransportFX transportFX = new TransportFX(transport.getTrId(), transport.getTrMark(), transport.getTrModel(), transport.getCategoryOfTransport().getCatTrName());
            transportFXES.add(transportFX);
        }
    }


    private Scene getActualScene(){
        return transportsTable.getScene();
    }

    private void setTooltipForButtons() {
        createButton.setOnMouseEntered(event->{
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_TRANSPORT")));
        });
        editButton.setOnMouseEntered(event->{
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_TRANSPORT")));
        });
        deleteButton.setOnMouseEntered(event->{
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_TRANSPORT")));
        });
        refreshButton.setOnMouseEntered(event->{
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_TRANSPORT")));
        });
        showFilterButton.setOnMouseEntered(event->{
            showFilterButton.setTooltip(new Tooltip(rb.getString("SHOW_LAST_FILTER")));
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
        Transport transport = null;
        //TransportFX selectedTransportFX = null;
        switch (operationMode){
            case CREATE:
                transport = new Transport();
                break;
            case EDIT:
            case DELETE:
                if(transportsTable.getSelectionModel().getSelectedItem() != null){
                    TransportFX selectedTransportFX = transportsTable.getSelectionModel().getSelectedItem();

                    transport = new Transport();
                    transport.setTrId(selectedTransportFX.getTrId());
                    transport.setTrMark(selectedTransportFX.getTrMark());
                    transport.setTrModel(selectedTransportFX.getTrModel());

                    CategoryOfTransport categoryOfTransport = new CategoryOfTransport();
                    categoryOfTransport.setCatTrName(selectedTransportFX.getTrCategory());
                    transport.setCategoryOfTransport(categoryOfTransport);
                }
                break;
        }
        if (transport == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_TRANSPORT"));
            transportsTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Transport.resources.EditTransport", "Transport/fxml/EditTransport.fxml", getActualScene());
                EditTransportController editTransportController = fxWindowData.getLoader().getController();

                editTransportController.setParameters(transport, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

                doResult(editTransportController.getExitMode(), operationMode,transport);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                transportsTable.requestFocus();
            }
        }
    }

    private void doResult(FXFormExitMode exitMode, FXOperationMode operationMode, Transport transport) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    int indexPage = transportFXES.isEmpty() ? 0 : pagination.getCurrentPageIndex();
                    fillingTable(indexPage);
                    Optional<TransportFX> transportFX = transportFXES.stream()
                            .filter(t->t.getTrId() == transport.getTrId())
                            .findFirst();

                    transportFX.ifPresent(fx -> transportsTable.getSelectionModel().select(fx));
                    modelColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    fillingTable(pagination.getCurrentPageIndex());
                    Optional<TransportFX> transportFXEd = transportFXES.stream()
                            .filter(t->t.getTrId() == transport.getTrId())
                            .findFirst();

                    transportFXEd.ifPresent(fx -> transportsTable.getSelectionModel().select(fx));
                    modelColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    fillingTable(pagination.getCurrentPageIndex());
                    break;
            }
        }

        transportsTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        filterMarkField.clear();
        filterModelField.clear();
        filterCategoryField.clear();

        filterCategory = "";
        filterModel = "";
        filterMark = "";

        //pagination.setCurrentPageIndex(0);
        fillingTable(0);
        pagination.setCurrentPageIndex(0);
        transportsTable.requestFocus();
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        //if((filterCategoryField.getText() != null && !filterCategoryField.getText().isBlank()) || (filterMarkField.getText() != null && !filterMarkField.getText().isBlank()) || (filterModelField.getText() != null && !filterModelField.getText().isBlank())){
            try{
                filterCategory = filterCategoryField.getText().trim();
                filterMark = filterMarkField.getText().trim();
                filterModel = filterModelField.getText().trim();

                //System.out.println(filterCategory + "\n" + filterMark + "\n" + filterModel);
                fillingTable(0);
                pagination.setCurrentPageIndex(0);

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                transportsTable.requestFocus();
            }
        //}else{
            //transportsTable.requestFocus();
        //}
    }

    public void showFilterButtonAction(ActionEvent actionEvent) {
        String message = String.format("%s:%s\n%s:%s\n%s:%s",rb.getString("TRANSPORT_CATEGORY"), filterCategory, rb.getString("MARK"),filterMark, rb.getString("MODEL"),filterModel);
        FXHelper.showInfoAlert(message);
    }
}
