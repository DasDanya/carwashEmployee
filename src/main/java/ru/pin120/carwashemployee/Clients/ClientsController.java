package ru.pin120.carwashemployee.Clients;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.ClientsTransport.ClientTransportController;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.*;

public class ClientsController implements Initializable {

    @FXML
    private ComboBox<String> operationDiscountComboBox;
    @FXML
    private Pagination pagination;
    @FXML
    private Spinner<Integer> discountSpinner;
    @FXML
    private TableColumn<ClientFX, String> surnameColumn;
    @FXML
    private TableColumn<ClientFX, String> nameColumn;
    @FXML
    private TableColumn<ClientFX, String> phoneColumn;
    @FXML
    private TableColumn<ClientFX, Long> idColumn;
    @FXML
    private TableColumn<ClientFX, Integer> saleColumn;
    @FXML
    private TableView<ClientFX> clientsTable;
    @FXML
    private TextField filterPhoneField;
    @FXML
    private TextField filterNameField;
    @FXML
    private TextField filterSurnameField;
    @FXML
    private Button showTransportButton;
    @FXML
    public Button showFilterParametersButton;
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
    private ResourceBundle rb;

    private ObservableList<ClientFX> clientFXES = FXCollections.observableArrayList();
    private ClientsRepository clientsRepository = new ClientsRepository();

    private String filterSurname = "";
    private String filterName = "";
    private String filterPhone = "";
    private Integer filterDiscount = null;
    private String filterDiscountOperator = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(c->c.getValue().clIdProperty().asObject());
        surnameColumn.setCellValueFactory(c->c.getValue().clSurnameProperty());
        nameColumn.setCellValueFactory(c->c.getValue().clNameProperty());
        phoneColumn.setCellValueFactory(c->c.getValue().clPhoneProperty());
        saleColumn.setCellValueFactory(c->c.getValue().clDiscountProperty().asObject());
        discountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, ClientFX.MAX_DISCOUNT,0,1));
        try {
            setSpinnerFormatter();
        }catch (Exception e){
        }

        FXHelper.setContextMenuForEditableTextField(filterSurnameField);
        FXHelper.setContextMenuForEditableTextField(filterNameField);
        FXHelper.setContextMenuForEditableTextField(filterPhoneField);

        List<String> operators = new ArrayList<>(Arrays.asList(" ","<", ">", "="));
        operationDiscountComboBox.getItems().setAll(operators);

        setTooltipForButton();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));

        fillingTable(0);
        pageIndexListener();
        filterPhoneListener();
    }

    private void filterPhoneListener(){
        filterPhoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String digitsOnly = newValue.replaceAll("\\D", "");

                if (digitsOnly.length() > ClientFX.MAX_PHONE_FILLING) {
                    digitsOnly = digitsOnly.substring(0, ClientFX.MAX_PHONE_FILLING);
                }

                filterPhoneField.setText(digitsOnly);
            }
        });
    }

    private void setSpinnerFormatter() {
        TextFormatter<Integer> discountFormatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= ClientFX.MAX_DISCOUNT) {
                    return change;
                }
            }
            return null;
        });

        discountSpinner.getEditor().setTextFormatter(discountFormatter);
    }

    private void fillingTable(int pageIndex){
        try{
            clientFXES.clear();
            List<Client> clients = new ArrayList<>();
            if(filterSurname.isBlank() && filterName.isBlank() && filterPhone.isBlank() && filterDiscountOperator.isBlank() && filterDiscount == null){
                clients = clientsRepository.getByPage(pageIndex);
            }else{
                clients = clientsRepository.search(pageIndex, filterSurname, filterName, filterPhone, filterDiscount, filterDiscountOperator);
            }
            fillingObservableList(clients);
            clientsTable.setItems(clientFXES);
            clientsTable.getSelectionModel().selectFirst();
            Platform.runLater(()->clientsTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            clientsTable.requestFocus();
        }
    }

    private void fillingObservableList(List<Client> clients) {
        for(Client client: clients){
            ClientFX clientFX = new ClientFX(client.getClId(), client.getClSurname(), client.getClName(), client.getClPhone(), client.getClDiscount());
            clientFXES.add(clientFX);
        }
    }

    private void pageIndexListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    private void setTooltipForButton() {
        createButton.setOnMouseEntered(event->{
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_CLIENT")));
        });
        editButton.setOnMouseEntered(event->{
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_CLIENT")));
        });
        deleteButton.setOnMouseEntered(event->{
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_CLIENT")));
        });
        refreshButton.setOnMouseEntered(event->{
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CLIENT")));
        });
        showFilterParametersButton.setOnMouseEntered(event->{
            showFilterParametersButton.setTooltip(new Tooltip(rb.getString("SHOW_LAST_FILTER")));
        });
        showTransportButton.setOnMouseEntered(event->{
            showTransportButton.setTooltip(new Tooltip(rb.getString("SHOW_TRANSPORT")));
        });
    }

    private Scene getActualScene(){
        return clientsTable.getScene();
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
        Client client = null;
        switch (operationMode){
            case CREATE:
                client = new Client();
                break;
            case EDIT:
            case DELETE:
                if(clientsTable.getSelectionModel().getSelectedItem() != null){
                    ClientFX clientFX = clientsTable.getSelectionModel().getSelectedItem();

                    client = new Client();
                    client.setClId(clientFX.getClId());
                    client.setClSurname(clientFX.getClSurname());
                    client.setClName(clientFX.getClName());
                    client.setClPhone(clientFX.getClPhone());
                    client.setClDiscount(clientFX.getClDiscount());
                }
                break;
        }
        if(client == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLIENT"));
            clientsTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Clients.resources.EditClient", "Clients/fxml/EditClient.fxml", getActualScene());
                EditClientController editClientController = fxWindowData.getLoader().getController();

                editClientController.setParameters(client, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
                
                doResult(editClientController.getExitMode(), operationMode, client);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                clientsTable.requestFocus();
            }
        }
    }

    private void doResult(FXFormExitMode exitMode, FXOperationMode operationMode, Client client) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    int indexPage = clientFXES.isEmpty() ? 0 : pagination.getCurrentPageIndex();
                    fillingTable(indexPage);
                    Optional<ClientFX> clientFX = clientFXES.stream()
                            .filter(t->t.getClId() == client.getClId())
                            .findFirst();

                    clientFX.ifPresent(fx -> clientsTable.getSelectionModel().select(fx));
                    surnameColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    fillingTable(pagination.getCurrentPageIndex());
                    Optional<ClientFX> clientFXEd = clientFXES.stream()
                            .filter(t->t.getClId() == client.getClId())
                            .findFirst();

                    clientFXEd.ifPresent(fx -> clientsTable.getSelectionModel().select(fx));
                    surnameColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    fillingTable(pagination.getCurrentPageIndex());
                    break;
            }
        }

        clientsTable.requestFocus();
    }


    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        filterSurnameField.clear();
        filterNameField.clear();
        filterPhoneField.clear();
        discountSpinner.getValueFactory().setValue(0);
        operationDiscountComboBox.getSelectionModel().selectFirst();

        filterSurname = "";
        filterName = "";
        filterPhone = "";
        filterDiscountOperator = "";
        filterDiscount = null;

        fillingTable(0);
        pagination.setCurrentPageIndex(0);
        clientsTable.requestFocus();
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            filterSurname = filterSurnameField.getText().trim();
            filterName = filterNameField.getText().trim();
            filterPhone = filterPhoneField.getText().trim();

            if(operationDiscountComboBox.getSelectionModel().getSelectedItem() != null) {
                filterDiscount = discountSpinner.getValue();
                filterDiscountOperator = operationDiscountComboBox.getSelectionModel().getSelectedItem();
            }

            fillingTable(0);
            pagination.setCurrentPageIndex(0);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            clientsTable.requestFocus();
        }
    }

    public void showFilterParametersButtonAction(ActionEvent actionEvent) {
        String message = String.format("%s:%s\n%s:%s\n%s:%s\n%s:%s\n%s:%s",rb.getString("CLIENT_SURNAME"), filterSurname, rb.getString("CLIENT_NAME"),filterName, rb.getString("PHONE"),filterPhone, rb.getString("COMPARING_OPERATOR"),
                filterDiscountOperator, rb.getString("DISCOUNT"), filterDiscount == null || filterDiscountOperator.isBlank() ? "" : filterDiscount.toString());
        FXHelper.showInfoAlert(message);
    }

    public void showTransportButtonAction(ActionEvent actionEvent) {
        try{
            ClientFX clientFX = clientsTable.getSelectionModel().getSelectedItem();
            if(clientFX == null){
                FXHelper.showErrorAlert(rb.getString("NOT_SELECT_CLIENT"));
                clientsTable.requestFocus();
            }else{
                Client client = new Client(clientFX.getClId(), clientFX.getClSurname(), clientFX.getClName(),clientFX.getClPhone(),clientFX.getClDiscount());
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.ClientsTransport.resources.ClientTransport", "ClientsTransport/fxml/ClientTransport.fxml", getActualScene());
                ClientTransportController clientTransportController = fxWindowData.getLoader().getController();
                clientTransportController.setParameters(client, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();

                clientsTable.requestFocus();
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            clientsTable.requestFocus();
        }
    }
}
