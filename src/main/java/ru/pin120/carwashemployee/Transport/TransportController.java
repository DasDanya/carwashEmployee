package ru.pin120.carwashemployee.Transport;

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

public class TransportController implements Initializable {

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(t->t.getValue().trIdProperty().asObject());
        markColumn.setCellValueFactory(t->t.getValue().trMarkProperty());
        modelColumn.setCellValueFactory(t->t.getValue().trModelProperty());
        categoryColumn.setCellValueFactory(t->t.getValue().trCategoryProperty());

        FXHelper.setContextMenuForTextField(filterMarkField);
        FXHelper.setContextMenuForTextField(filterModelField);
        FXHelper.setContextMenuForTextField(filterCategoryField);

        setTooltipForButton();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    private Scene getActualScene(){
        return transportsTable.getScene();
    }

    private void setTooltipForButton() {
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
        filterMarkField.clear();
        filterModelField.clear();
        filterCategoryField.clear();

        transportsTable.requestFocus();
    }

    public void searchButtonAction(ActionEvent actionEvent) {
    }
}
