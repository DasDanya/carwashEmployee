package ru.pin120.carwashemployee.ClientsTransport;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.Transport.Transport;
import ru.pin120.carwashemployee.Transport.TransportFX;
import ru.pin120.carwashemployee.Transport.TransportRepository;

import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditClientTransportController implements Initializable {

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
    private Button searchButton;
    @FXML
    private TextField filterCategoryField;
    @FXML
    private TextField filterModelField;
    @FXML
    private TextField filterMarkField;
    @FXML
    private TextField stateNumberField;
    private ResourceBundle rb;

    private ClientsTransport clientsTransport;
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    @FXML
    private Stage stage;

    private TransportRepository transportRepository = new TransportRepository();
    private ClientsTransportRepository clientsTransportRepository = new ClientsTransportRepository();

    List<Transport> transports;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        idColumn.setCellValueFactory(t->t.getValue().trIdProperty().asObject());
        markColumn.setCellValueFactory(t->t.getValue().trMarkProperty());
        modelColumn.setCellValueFactory(t->t.getValue().trModelProperty());
        categoryColumn.setCellValueFactory(t->t.getValue().trCategoryProperty());

        FXHelper.setContextMenuForEditableTextField(filterCategoryField);
        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(filterMarkField);
        FXHelper.setContextMenuForEditableTextField(stateNumberField);

        stateNumberFieldListener();
        pageIndexListener();
        setTooltipForButton();
    }

    private void pageIndexListener(){
        pagination.currentPageIndexProperty().addListener((obs, oldIndex, newIndex) -> {
            fillingTable(newIndex.intValue());
        });
    }

    private void fillingTable(int pageIndex){
        try{
            transportsTable.getItems().clear();
            if(filterCategoryField.getText().isBlank() && filterMarkField.getText().isBlank() && filterModelField.getText().isBlank()){
                transports = transportRepository.getByPage(pageIndex);
            }else{
                transports = transportRepository.search(pageIndex, filterCategoryField.getText().trim(),filterMarkField.getText().trim(),filterModelField.getText().trim());
            }

            ObservableList<TransportFX> transportFXES = FXCollections.observableArrayList();
            for(Transport transport: transports){
                TransportFX transportFX = new TransportFX(transport.getTrId(), transport.getTrMark(), transport.getTrModel(), transport.getCategoryOfTransport().getCatTrName());
                transportFXES.add(transportFX);
            }

            transportsTable.setItems(transportFXES);
            transportsTable.getSelectionModel().selectFirst();
            Platform.runLater(()->transportsTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            transportsTable.requestFocus();
        }
    }


    private void setTooltipForButton() {
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_TRANSPORT")));
        });
    }

    private void stateNumberFieldListener() {
        stateNumberField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientsTransportFX.MAX_STATE_NUMBER_LENGTH) {
                    stateNumberField.setText(oldValue);
                }
            }
        });
    }

    public void setParameters(ClientsTransport clientsTransport, FXOperationMode operationMode, Stage modalStage) {
        this.clientsTransport = clientsTransport;
        this.operationMode = operationMode;
        this.stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                fillingTable(0);
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                fillingComponents();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                stateNumberField.setDisable(true);
                filterMarkField.setDisable(true);
                filterModelField.setDisable(true);
                filterCategoryField.setDisable(true);
                searchButton.setDisable(true);
                transportsTable.setDisable(true);
                pagination.setDisable(true);
                fillingComponents();
                break;
        }

        closeWindowAction();

    }

    private void fillingComponents() {
        stateNumberField.setText(clientsTransport.getClTrStateNumber());
        filterMarkField.setText(clientsTransport.getTransport().getTrMark());
        filterModelField.setText(clientsTransport.getTransport().getTrModel());
        filterCategoryField.setText(clientsTransport.getTransport().getCategoryOfTransport().getCatTrName());

        fillingTable(0);
    }


    public void searchButtonAction(ActionEvent actionEvent) {
        try{
            fillingTable(0);
            pagination.setCurrentPageIndex(0);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            transportsTable.requestFocus();
        }
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT) {
            TransportFX transportFX = transportsTable.getSelectionModel().getSelectedItem();
            if (transportFX == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECT_TRANSPORT"));
                transportsTable.requestFocus();
            } else {
                if (stateNumberField.getText() == null || stateNumberField.getText().isBlank()) {
                    FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_STATENUMBER"));
                    stateNumberField.requestFocus();
                } else {
                    String stateNumber = stateNumberField.getText().trim().toUpperCase(Locale.getDefault());
                    boolean validStateNumber = isValidStateNumber(transportFX, stateNumber);
                    if (validStateNumber) {
                        clientsTransport.setClTrStateNumber(stateNumber);
                        Optional<Transport> transport = transports.stream()
                                .filter(t-> t.getTrId() == transportFX.getTrId())
                                .findFirst();
                        transport.ifPresent(t->clientsTransport.setTransport(t));

                        try {
                            if (operationMode == FXOperationMode.CREATE) {
                                ClientsTransport createdTransport = clientsTransportRepository.create(clientsTransport);
                                if(createdTransport != null){
                                    clientsTransport.setClTrId(createdTransport.getClTrId());
                                    canExit = true;
                                }
                            }else{
                                if(clientsTransportRepository.edit(clientsTransport) != null){
                                    canExit = true;
                                }
                            }
                        }catch (Exception e){
                            FXHelper.showErrorAlert(e.getMessage());
                        }
                    }
                }
            }
        }else if(operationMode == FXOperationMode.DELETE){
            try {
                canExit = clientsTransportRepository.delete(clientsTransport.getClTrId());
            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
    }

    private boolean isValidStateNumber(TransportFX transportFX, String stateNumber) {
        boolean validStateNumber = true;
        if (transportFX.getTrCategory().equalsIgnoreCase("мотоцикл") || transportFX.getTrCategory().equalsIgnoreCase("квадроцикл") || transportFX.getTrCategory().equalsIgnoreCase("трактор")) {
            if (!stateNumber.matches(ClientsTransportFX.MOTO_AGR_REGEX)) {
                validStateNumber = false;
                FXHelper.showErrorAlert(rb.getString("NOT_VALID_MOTO_AGR"));
            }
        } else if (!stateNumber.matches(ClientsTransportFX.CAR_REGEX)) {
            validStateNumber = false;
            FXHelper.showErrorAlert(rb.getString("NOT_VALID_AUTO"));
        }
        return validStateNumber;
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
