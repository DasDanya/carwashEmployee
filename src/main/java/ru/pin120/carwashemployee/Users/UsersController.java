package ru.pin120.carwashemployee.Users;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import ru.pin120.carwashemployee.Boxes.BoxFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.SuppliesInBox.SuppliesInBoxController;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Контроллер формы с пользователями
 */
public class UsersController implements Initializable {
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<UserFX> usersTable;
    @FXML
    private TableColumn<UserFX,String> nameColumn;
    @FXML
    private TableColumn<UserFX,String> roleColumn;
    private ResourceBundle rb;
    private ObservableList<UserFX> userFXES = FXCollections.observableArrayList();
    private UsersRepository usersRepository = new UsersRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        nameColumn.setCellValueFactory(us->us.getValue().usNameProperty());
        roleColumn.setCellValueFactory(us->us.getValue().usRoleProperty());
        usersTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        fillingAll();


        setTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    private void fillingAll() {
        userFXES.clear();
        try{
            List<User> users = usersRepository.get();
            for(User user: users){
                UserFX userFX = new UserFX(user.getUsId(), user.getUsName(), user.getUsRole().getDisplayValue());
                userFXES.add(userFX);
            }

            usersTable.setItems(userFXES);
            usersTable.getSelectionModel().selectFirst();
            Platform.runLater(()->usersTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            usersTable.requestFocus();
        }
    }

    private Scene getActualScene(){
        return usersTable.getScene();
    }

    private void setTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_USER")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_USER")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_PASSWORD")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
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
        User user = null;
        UserFX selectedUserFX = null;
        switch (operationMode){
            case CREATE:
                user = new User();
                break;
            case EDIT:
            case DELETE:
                selectedUserFX = usersTable.getSelectionModel().getSelectedItem();
                if(selectedUserFX != null){
                    user = new User();
                    user.setUsId(user.getUsId());
                    user.setUsName(selectedUserFX.getUsName());
                    user.setUsRole(UserRole.valueOfDisplayValue(selectedUserFX.getUsRole()));
                }
                break;
        }
        if(user == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_USER"));
            usersTable.requestFocus();
        }else {
            boolean canOpenWindow = true;
            if(operationMode == FXOperationMode.DELETE) {
                if (user.getUsRole() == UserRole.OWNER) {
                    FXHelper.showErrorAlert(String.format(rb.getString("CANNOT_DELETE_USER"), UserRole.OWNER.getDisplayValue()));
                    usersTable.requestFocus();
                    canOpenWindow = false;
                }
            }
            if(canOpenWindow){
                try {
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Users.resources.EditUser", "Users/fxml/EditUser.fxml", getActualScene());
                    EditUserController editUserController = fxWindowData.getLoader().getController();
                    editUserController.setParameters(operationMode, user, fxWindowData.getModalStage());
                    fxWindowData.getModalStage().showAndWait();
                    doResult(operationMode, editUserController.getExitMode(), user, selectedUserFX);
                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    usersTable.requestFocus();
                }
            }
        }
    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, User user,UserFX selectedUserFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    UserFX userFX = new UserFX(user.getUsId(), user.getUsName(), user.getUsRole().getDisplayValue());
                    userFXES.add(userFX);
                    userFXES.sort(Comparator.comparing(UserFX::getUsRole, Comparator.reverseOrder())
                            .thenComparing(UserFX::getUsName));

                    usersTable.getSelectionModel().select(userFX);
                    break;
                case DELETE:
                    userFXES.remove(selectedUserFX);
                    break;
                case EDIT:
                    FXHelper.showInfoAlert(rb.getString("SUCCESS_EDIT_PASSWORD"));
                    break;
            }
        }

        usersTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        fillingAll();
    }
}
