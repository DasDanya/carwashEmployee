package ru.pin120.carwashemployee.Main;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private MenuBar menuBar;
    private ResourceBundle rb;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }

    private Stage getStage(){
        return (Stage) menuBar.getScene().getWindow();
    }

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

    public void showBookingsMenuItem(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Bookings.resources.Bookings", "Bookings/fxml/Bookings.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("BOOKINGS_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    public void showClientsMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Clients.resources.Clients", "Clients/fxml/Clients.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CLIENTS_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

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

    public void showWorkScheduleMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.WorkSchedule.resources.WorkSchedule", "WorkSchedule/fxml/WorkSchedule.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("WORKSCHEDULE_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    public void showCategoryTransportMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesOfTransport.resources.CategoriesOfTransport", "CategoriesOfTransport/fxml/CategoriesOfTransport.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_OF_TRANSPORT_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    public void showTransportMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.Transport.resources.Transport", "Transport/fxml/Transport.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("TRANSPORT_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    public void showCategoriesAndServicesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesAndServices.resources.CategoriesAndServices", "CategoriesAndServices/fxml/CategoriesAndServices.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_AND_SERVICES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    public void showCategoriesOfSuppliesMenuItemAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createWindow("ru.pin120.carwashemployee.CategoriesOfSupplies.resources.CategoriesOfSupplies", "CategoriesOfSupplies/fxml/CategoriesOfSupplies.fxml");
            fxWindowData.getModalStage().setTitle(rb.getString("CATEGORIES_OF_SUPPLIES_FORM_TITLE"));
            fxWindowData.getModalStage().show();
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    public void showSuppliesMenuItemAction(ActionEvent actionEvent) {
    }
}
