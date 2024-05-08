package ru.pin120.carwashemployee.Clients;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientsController implements Initializable {

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
    private ComboBox<Integer> filterSaleComboBox;
    @FXML
    private TextField filterPhoneField;
    @FXML
    private TextField filterNameField;
    @FXML
    private TextField filterSurnameField;
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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(c->c.getValue().clIdProperty().asObject());
        surnameColumn.setCellValueFactory(c->c.getValue().clSurnameProperty());
        nameColumn.setCellValueFactory(c->c.getValue().clNameProperty());
        phoneColumn.setCellValueFactory(c->c.getValue().clPhoneProperty());
        saleColumn.setCellValueFactory(c->c.getValue().clSaleProperty().asObject());

        FXHelper.setContextMenuForTextField(filterSurnameField);
        FXHelper.setContextMenuForTextField(filterNameField);
        FXHelper.setContextMenuForTextField(filterPhoneField);

        setTooltipForButton();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
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

    }


    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        filterSurnameField.clear();
        filterNameField.clear();
        filterPhoneField.clear();
        filterSaleComboBox.getSelectionModel().selectFirst();

        clientsTable.requestFocus();
    }

    public void searchButtonAction(ActionEvent actionEvent) {
    }
}
