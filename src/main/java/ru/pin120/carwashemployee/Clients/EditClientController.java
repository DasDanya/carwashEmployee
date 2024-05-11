package ru.pin120.carwashemployee.Clients;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPositionFX;
import ru.pin120.carwashemployee.Transport.TransportFX;

import java.net.URL;
import java.util.ResourceBundle;

public class EditClientController implements Initializable {
    @FXML
    private TextField surnameField;
    @FXML
    private TextField nameField;
    @FXML
    private TextField phoneField;
    @FXML
    private Spinner<Integer> discountSpinner;
    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    private ResourceBundle rb;
    @FXML
    private Stage stage;

    @Getter
    private FXFormExitMode exitMode;
    private FXOperationMode operationMode;
    private ClientsRepository clientsRepository = new ClientsRepository();
    private Client client;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        discountSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, ClientFX.MAX_DISCOUNT,0,1));
        FXHelper.setContextMenuForTextField(surnameField);
        FXHelper.setContextMenuForTextField(nameField);
        FXHelper.setContextMenuForTextField(phoneField);

        textListeners();
        try {
            setSpinnerFormatter();
        }catch (Exception e){
        }
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

    public void setParameters(Client client, FXOperationMode operationMode, Stage stage){
        this.client = client;
        this.operationMode = operationMode;
        this.stage = stage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                fillingComponents();
                Platform.runLater(()->discountSpinner.getEditor().positionCaret(discountSpinner.getEditor().getText().length()));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                fillingComponents();
                surnameField.setDisable(true);
                nameField.setDisable(true);
                phoneField.setDisable(true);
                discountSpinner.setDisable(true);
                break;
        }

        this.stage.setMaxHeight(210);
        closeWindowAction();
    }


    private void textListeners(){
        surnameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientFX.MAX_SURNAME_LENGTH) {
                    surnameField.setText(oldValue);
                }
            }
        });
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > ClientFX.MAX_NAME_LENGTH) {
                    nameField.setText(oldValue);
                }
            }
        });
        phoneField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                String digitsOnly = newValue.replaceAll("\\D", "");

                if (digitsOnly.length() > ClientFX.MAX_PHONE_FILLING) {
                    digitsOnly = digitsOnly.substring(0, ClientFX.MAX_PHONE_FILLING);
                }

                phoneField.setText(digitsOnly);
            }
        });
    }

    private void fillingComponents() {
        surnameField.setText(client.getClSurname());
        nameField.setText(client.getClName());
        phoneField.setText(client.getClPhone().substring(2));
        discountSpinner.getValueFactory().setValue(client.getClDiscount());
    }


    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(surnameField.getText() == null || surnameField.getText().isBlank()){
            surnameField.clear();
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_SURNAME"));
            surnameField.requestFocus();
        }else if(nameField.getText() == null || nameField.getText().isBlank()){
            nameField.clear();
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_NAME"));
            nameField.requestFocus();
        }else if(phoneField.getText() == null || phoneField.getText().isBlank() || phoneField.getText().length() < ClientFX.MAX_PHONE_FILLING){
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_PHONE"));
            phoneField.requestFocus();
        }else if(!surnameField.getText().matches(ClientFX.SURNAME_REGEX)){
            FXHelper.showErrorAlert(rb.getString("SURNAME_VALID_CHARACTERS"));
            nameField.requestFocus();
        }else if(!nameField.getText().matches(ClientFX.NAME_REGEX)){
            FXHelper.showErrorAlert(rb.getString("NAME_VALID_CHARACTERS"));
            nameField.requestFocus();
        }else{
            try{
                switch (operationMode){
                    case CREATE:
                        client.setClSurname(surnameField.getText().trim());
                        client.setClName(nameField.getText().trim());
                        client.setClPhone("+7"+phoneField.getText().trim());
                        client.setClDiscount(discountSpinner.getValue());

                        Client createdClient = clientsRepository.create(client);
                        if(createdClient != null){
                            canExit = true;
                            client.setClId(createdClient.getClId());
                        }
                        break;
                    case EDIT:
                        String clPhone = client.getClPhone().substring(2);
                        if(client.getClSurname().equals(surnameField.getText().trim()) && client.getClName().equals(nameField.getText().trim())
                                && clPhone.equals(phoneField.getText().trim()) && client.getClDiscount().intValue() == discountSpinner.getValue().intValue()){
                            canExit = true;
                        }else {
                            client.setClSurname(surnameField.getText().trim());
                            client.setClName(nameField.getText().trim());
                            client.setClPhone("+7" + phoneField.getText().trim());
                            client.setClDiscount(discountSpinner.getValue());
                            if(clientsRepository.edit(client) != null){
                                canExit = true;
                            }
                        }
                        break;
                    case DELETE:
                        canExit = clientsRepository.delete(client.getClId());
                        break;
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
