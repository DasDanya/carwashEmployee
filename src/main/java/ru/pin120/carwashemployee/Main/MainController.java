package ru.pin120.carwashemployee.Main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Users.UserRole;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * ���������� ������� ����� ����������
 */
public class MainController implements Initializable {
    @FXML
    private Label welcomeLabel;
    private ResourceBundle rb;

    /**
     * ������������� �����������
     *
     * @param url URL ������������ FXML �����
     * @param resourceBundle ����� �������� ��� �����������
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        String welcomeText = String.format(rb.getString("WELCOME"), AppHelper.getUserInfo().get(1));
        welcomeLabel.setText(welcomeText);
        Platform.runLater(()->getStage().setTitle(rb.getString("FORM_TITLE")));
    }

    /**
     * �������� ������� Stage
     *
     * @return ������� Stage.
     */
    private Stage getStage(){
        return (Stage) welcomeLabel.getScene().getWindow();
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � �������
     * @param actionEvent ������� ��������
     */
    public void showBoxesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Boxes.resources.Boxes", "Boxes/fxml/Boxes.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("BOXES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
            //getStage().close();

        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }

    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ��������
     * @param actionEvent ������� ��������
     */
    public void showBookingsMenuItem(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.Bookings", "Bookings/fxml/Bookings.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("BOOKINGS_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ���������
     * @param actionEvent ������� ��������
     */
    public void showClientsMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Clients.resources.Clients", "Clients/fxml/Clients.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CLIENTS_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ���������
     * @param actionEvent ������� ��������
     */
    public void showCleanersMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Cleaners.resources.Cleaners", "Cleaners/fxml/Cleaners.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CLEANERS_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            e.printStackTrace();
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � �������� ����� ��������
     * @param actionEvent ������� ��������
     */
    public void showWorkScheduleMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.WorkSchedule.resources.WorkSchedule", "WorkSchedule/fxml/WorkSchedule.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("WORKSCHEDULE_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ����������� ����������
     * @param actionEvent ������� ��������
     */
    public void showCategoryTransportMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesOfTransport.resources.CategoriesOfTransport", "CategoriesOfTransport/fxml/CategoriesOfTransport.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_OF_TRANSPORT_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � �����������
     * @param actionEvent ������� ��������
     */
    public void showTransportMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Transport.resources.Transport", "Transport/fxml/Transport.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("TRANSPORT_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ����������� � �������� ���������
     * @param actionEvent ������� ��������
     */
    public void showCategoriesAndServicesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesAndServices.resources.CategoriesAndServices", "CategoriesAndServices/fxml/CategoriesAndServices.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_AND_SERVICES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ����������� ��������� ����������
     * @param actionEvent ������� ��������
     */
    public void showCategoriesOfSuppliesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesOfSupplies.resources.CategoriesOfSupplies", "CategoriesOfSupplies/fxml/CategoriesOfSupplies.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_OF_SUPPLIES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ���������� �����������
     * @param actionEvent ������� ��������
     */
    public void showSuppliesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Supplies.resources.Supplies", "Supplies/fxml/Supplies.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("SUPPLIES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * ���������� ������� �� ���� ��� �������� ����� � ��������������
     * @param actionEvent ������� ��������
     */
    public void showUsersMenuItemAction(ActionEvent actionEvent) {
        try {
            if(AppHelper.getUserInfo().get(2).equals(UserRole.OWNER.name())){
                FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Users.resources.Users", "Users/fxml/Users.fxml");
                fxWindowData.getModalStage().setTitle(rb.getString("USERS_FORM_TITLE"));
                fxWindowData.getModalStage().show();
            }else{
                FXHelper.showErrorAlert(AppHelper.getCannotAccessFormText());
            }
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    /**
     * ���������� �������� ��� ������ �� ������� ������.
     * ��������� ��� ���� � ��������� ���� �����������.
     *
     * @param actionEvent ������� ��������.
     */
    public void exitButtonAction(ActionEvent actionEvent) {
        List<Stage> stagesToClose = new ArrayList<>();
        for (Window window : Stage.getWindows()) {
            stagesToClose.add((Stage)  window);
        }
        for (Stage stage : stagesToClose) {
            stage.close();
        }

        //AppHelper.getUserInfo().clear(); // ������� ������ �������������
        System.out.println(AppHelper.getUserInfo().get(0));
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Users.resources.UserAuthorization", "Users/fxml/UserAuthorization.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("USERS_FORM_TITLE"));
            ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.Users.resources.UserAuthorization", Locale.getDefault());

            fxWindowData.getModalStage().setTitle(bundle.getString("FORM_TITLE"));
            fxWindowData.getModalStage().show();

        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }
}
