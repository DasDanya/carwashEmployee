package ru.pin120.carwashemployee.Users;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Http.LoginRequest;

import java.net.URL;
import java.util.ResourceBundle;

public class UserAuthorizationController implements Initializable {
    @FXML
    private TextField nameField;
    @FXML
    private TextField passwordShowField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private CheckBox showCheckBox;
    private ResourceBundle rb;
    private UsersRepository usersRepository = new UsersRepository();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        FXHelper.setContextMenuForEditableTextField(nameField);
        FXHelper.setContextMenuForEditableTextField(passwordShowField);
        FXHelper.setContextMenuForEditableTextField(passwordField);

        fieldsListeners();
        passwordShowField.textProperty().bindBidirectional(passwordField.textProperty());
        showPassword();
    }

    private void showPassword(){
        showCheckBox.setOnAction(event -> {
            if (showCheckBox.isSelected()) {
                passwordShowField.setVisible(true);
                passwordField.setVisible(false);
            } else {
                passwordShowField.setVisible(false);
                passwordField.setVisible(true);
            }
        });
    }

    private void fieldsListeners() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() > Integer.parseInt(rb.getString("MAX_LENGTH_NAME"))) {
                    nameField.setText(oldValue);
                }
            }
        });
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() > Integer.parseInt(rb.getString("MAX_LENGTH_PASSWORD"))) {
                    passwordField.setText(oldValue);
                }
            }
        });
        passwordShowField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                if (newValue.length() > Integer.parseInt(rb.getString("MAX_LENGTH_PASSWORD"))) {
                    passwordShowField.setText(oldValue);
                }
            }
        });

    }

    private Stage getStage(){
        return (Stage) nameField.getScene().getWindow();
    }

    public void btOKAction(ActionEvent actionEvent) {
        if(nameField.getText() == null || nameField.getText().isBlank()){
            nameField.clear();
            FXHelper.showErrorAlert(rb.getString("NAME_NOT_EMPTY"));
            nameField.requestFocus();
        }else if(passwordField.getText() == null || passwordField.getText().isBlank()){
            passwordField.clear();
            FXHelper.showErrorAlert(rb.getString("PASSWORD_NOT_EMPTY"));
            passwordField.requestFocus();
        }else{
            LoginRequest loginRequest = new LoginRequest(nameField.getText().trim(), passwordField.getText().trim());
            try {
                boolean successLogin = usersRepository.login(loginRequest);
                if(successLogin){
                    FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Main.resources.Main", "Main/fxml/Main.fxml");
                    getStage().close();
                    fxWindowData.getModalStage().show();
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }

        }
    }


}
