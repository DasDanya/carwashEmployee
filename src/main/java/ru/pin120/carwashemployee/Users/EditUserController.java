package ru.pin120.carwashemployee.Users;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.Aes;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

public class EditUserController implements Initializable {

    private final String NAME_REGEX="^[a-zA-Z0-9]{5,}$";
    private final String PASSWORD_REGEX= "^[a-zA-Z0-9@\\\\#$%&*()_+\\]\\[';:?.,!^-]{8,}$";
    @FXML
    private CheckBox showCheckBox;
    @FXML
    private TextField passwordShowField;
    @FXML
    private TextField nameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ComboBox<UserRole> roleComboBox;
    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    @Getter
    private FXFormExitMode exitMode;
    private FXOperationMode operationMode = FXOperationMode.CREATE;
    private UsersRepository usersRepository = new UsersRepository();

    private User user;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        FXHelper.setContextMenuForEditableTextField(nameField);
        FXHelper.setContextMenuForEditableTextField(passwordShowField);
        FXHelper.setContextMenuForEditableTextField(passwordField);

        fieldsListeners();
        //roleComboBox.getItems().addAll(UserRole.ADMINISTRATOR);
        roleComboBox.getItems().addAll(UserRole.values());
        roleComboBox.getSelectionModel().selectFirst();
        roleComboBox.setConverter(new StringConverter<UserRole>() {
            @Override
            public String toString(UserRole role) {
                return role.getDisplayValue();
            }
            @Override
            public UserRole fromString(String string) {
                return null;
            }
        });

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



    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(nameField.getText() == null || nameField.getText().isBlank()){
            nameField.clear();
            FXHelper.showErrorAlert(rb.getString("NAME_NOT_EMPTY"));
            nameField.requestFocus();
        }else if(passwordField.getText() == null || passwordField.getText().isBlank()){
            passwordField.clear();
            FXHelper.showErrorAlert(rb.getString("PASSWORD_NOT_EMPTY"));
            passwordField.requestFocus();
        }else if(!nameField.getText().matches(NAME_REGEX)){
            FXHelper.showErrorAlert(rb.getString("NOT_VALID_NAME"));
            nameField.requestFocus();
        }else if(!passwordField.getText().matches(PASSWORD_REGEX)) {
            FXHelper.showErrorAlert(rb.getString("NOT_VALID_PASSWORD"));
            passwordField.requestFocus();
        }else{
            try {
                switch (operationMode) {
                    case CREATE:
                        RegisterRequest registerRequest = new RegisterRequest(nameField.getText().trim(), passwordField.getText().trim(),roleComboBox.getSelectionModel().getSelectedItem().name());
                        User createdUser = usersRepository.register(registerRequest);
                        if(createdUser != null){
                            canExit = true;
                            user.setUsId(createdUser.getUsId());
                            user.setUsRole(createdUser.getUsRole());
                            user.setUsName(createdUser.getUsName());
                        }
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



    public void setParameters(FXOperationMode operationMode, User user, Stage modalStage) {
        this.operationMode = operationMode;
        this.user = user;
        this.stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                break;
        }

        this.stage.setMaxHeight(170);
        closeWindowAction();
    }


    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
