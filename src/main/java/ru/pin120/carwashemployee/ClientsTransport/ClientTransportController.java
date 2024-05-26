package ru.pin120.carwashemployee.ClientsTransport;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Transport.Transport;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class ClientTransportController implements Initializable {

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
    private TextField filterMarkField;
    @FXML
    private TextField filterModelField;
    @FXML
    private TextField filterCategoryField;
    @FXML
    private TextField filterStateNumberField;
    @FXML
    private TableView<ClientsTransportFX> clientTransportTable;
    @FXML
    private TableColumn<ClientsTransportFX, String> markColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> modelColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> categoryColumn;
    @FXML
    private TableColumn<ClientsTransportFX, String> stageNumberColumn;
    @FXML
    private TableColumn<ClientsTransportFX, Long> idColumn;
    private ResourceBundle rb;
    @FXML
    private Stage stage;
    private Client client;

    private ClientsTransportRepository clientsTransportRepository = new ClientsTransportRepository();
    private ObservableList<ClientsTransportFX> clientsTransportFXES = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(ct->ct.getValue().clTrIdProperty().asObject());
        modelColumn.setCellValueFactory(ct->ct.getValue().clTrModelProperty());
        markColumn.setCellValueFactory(ct->ct.getValue().clTrMarkProperty());
        categoryColumn.setCellValueFactory(ct->ct.getValue().clTrCategoryProperty());
        stageNumberColumn.setCellValueFactory(ct->ct.getValue().clTrStateNumberProperty());
        clientTransportTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(filterModelField);
        FXHelper.setContextMenuForEditableTextField(filterCategoryField);
        FXHelper.setContextMenuForEditableTextField(filterStateNumberField);

        setTooltipForButtons();
        Platform.runLater(this::fillingAll);
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));

        filterStateNumberFieldListener();
    }

    private void filterStateNumberFieldListener() {
        filterStateNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientsTransportFX.MAX_STATE_NUMBER_LENGTH) {
                    filterStateNumberField.setText(oldValue);
                }
            }
        });
    }


    private void fillingAll() {
        try{
            List<ClientsTransport> clientTransport = clientsTransportRepository.getByClientId(client.getClId());
            fillingObservableList(clientTransport);
            clientTransportTable.setItems(clientsTransportFXES);
            clientTransportTable.requestFocus();
            clientTransportTable.getSelectionModel().selectFirst();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<ClientsTransport> clientTransport) {
        for(ClientsTransport ct: clientTransport){
            ClientsTransportFX clientsTransportFX = new ClientsTransportFX(ct.getClTrId(), ct.getTransport().getTrMark(), ct.getTransport().getTrModel(), ct.getTransport().getCategoryOfTransport().getCatTrName(), ct.getClTrStateNumber());
            clientsTransportFXES.add(clientsTransportFX);
        }
    }

    private Scene getActualScene(){
        return clientTransportTable.getScene();
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
    }


    public void setParameters(Client client, Stage modalStage) {
        this.client = client;
        this.stage = modalStage;

        stage.setTitle(rb.getString("FORM_TITLE")  + this.client.getClSurname() + " " + this.client.getClName());
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
        ClientsTransport clientsTransport = null;
        ClientsTransportFX selectedClientTransportFX = null;

        switch (operationMode){
            case CREATE:
                clientsTransport = new ClientsTransport();
                clientsTransport.setClient(client);
                break;
            case EDIT:
            case DELETE:
                if(clientTransportTable.getSelectionModel().getSelectedItem() != null){
                    selectedClientTransportFX = clientTransportTable.getSelectionModel().getSelectedItem();
                    clientsTransport = new ClientsTransport();
                    clientsTransport.setClient(client);
                    clientsTransport.setClTrId(selectedClientTransportFX.getClTrId());
                    clientsTransport.setClTrStateNumber(selectedClientTransportFX.getClTrStateNumber());

                    Transport transport = new Transport();
                    transport.setTrMark(selectedClientTransportFX.getClTrMark());
                    transport.setTrModel(selectedClientTransportFX.getClTrModel());

                    CategoryOfTransport categoryOfTransport = new CategoryOfTransport();
                    categoryOfTransport.setCatTrName(selectedClientTransportFX.getClTrCategory());
                    transport.setCategoryOfTransport(categoryOfTransport);

                    clientsTransport.setTransport(transport);
                }
                break;
        }
        if(clientsTransport == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_TRANSPORT"));
            clientTransportTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.ClientsTransport.resources.EditClientTransport", "ClientsTransport/fxml/EditClientTransport.fxml", getActualScene());
                EditClientTransportController editClientTransportController = fxWindowData.getLoader().getController();

                editClientTransportController.setParameters(clientsTransport, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

                doResult(clientsTransport, selectedClientTransportFX, editClientTransportController.getExitMode(), operationMode);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                clientTransportTable.requestFocus();
            }
        }
    }

    private void doResult(ClientsTransport ct, ClientsTransportFX selectedClientTransportFX, FXFormExitMode exitMode, FXOperationMode operationMode) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    ClientsTransportFX clientsTransportFX = new ClientsTransportFX(ct.getClTrId(), ct.getTransport().getTrMark(), ct.getTransport().getTrModel(), ct.getTransport().getCategoryOfTransport().getCatTrName(), ct.getClTrStateNumber());
                    clientsTransportFXES.add(clientsTransportFX);
                    clientsTransportFXES.sort(Comparator.comparing(ClientsTransportFX::getClTrMark, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrModel, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrCategory, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrStateNumber,String::compareToIgnoreCase));

                    clientTransportTable.getSelectionModel().select(clientsTransportFX);
                    markColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    selectedClientTransportFX.setClTrCategory(ct.getTransport().getCategoryOfTransport().getCatTrName());
                    selectedClientTransportFX.setClTrMark(ct.getTransport().getTrMark());
                    selectedClientTransportFX.setClTrModel(ct.getTransport().getTrModel());
                    selectedClientTransportFX.setClTrStateNumber(ct.getClTrStateNumber());

                    clientsTransportFXES.sort(Comparator.comparing(ClientsTransportFX::getClTrMark, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrModel, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrCategory, String::compareToIgnoreCase)
                            .thenComparing(ClientsTransportFX::getClTrStateNumber,String::compareToIgnoreCase));

                    clientTransportTable.getSelectionModel().select(selectedClientTransportFX);
                    markColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    clientsTransportFXES.remove(selectedClientTransportFX);
                    break;
            }
        }

        clientTransportTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        filterCategoryField.clear();
        filterMarkField.clear();
        filterModelField.clear();
        filterStateNumberField.clear();

        clientsTransportFXES.clear();
        fillingAll();

        clientTransportTable.getSelectionModel().selectFirst();
        clientTransportTable.requestFocus();
        markColumn.setSortType(TableColumn.SortType.ASCENDING);
    }


    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            clientsTransportFXES.clear();
            List<ClientsTransport> clientTransport = clientsTransportRepository.search(client.getClId(), filterMarkField.getText().trim(), filterModelField.getText().trim(), filterCategoryField.getText().trim(), filterStateNumberField.getText().trim());
            fillingObservableList(clientTransport);
            clientTransportTable.setItems(clientsTransportFXES);
            clientTransportTable.requestFocus();
            clientTransportTable.getSelectionModel().selectFirst();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }
}
