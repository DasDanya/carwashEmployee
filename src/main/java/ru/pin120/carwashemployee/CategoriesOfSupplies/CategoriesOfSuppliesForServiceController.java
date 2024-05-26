package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.CategoriesAndServices.CategoryOfServicesFX;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.CategoriesAndServices.ServiceRepository;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesOfSuppliesForServiceController implements Initializable {

    @FXML
    private Button btSave;
    @FXML
    private Button refreshButton;
    @FXML
    private TableView<CategoryOfSuppliesForServiceFX> suppliesForServiceTable;
    @FXML
    private TableColumn<CategoryOfSuppliesForServiceFX, CheckBox> selectColumn;
    @FXML
    private TableColumn<CategoryOfSuppliesForServiceFX, String> categoriesOfSuppliesColumn;
    private ResourceBundle rb;
    private Service service;
    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();
    private ServiceRepository serviceRepository = new ServiceRepository();
    private ObservableList<CategoryOfSuppliesForServiceFX> suppliesForServiceFXES = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        selectColumn.setCellValueFactory(c->c.getValue().selectProperty());
        categoriesOfSuppliesColumn.setCellValueFactory(c->c.getValue().csupNameProperty());
        suppliesForServiceTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });

        Platform.runLater(()->FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doRefresh));
    }

    private Scene getActualScene(){
        return suppliesForServiceTable.getScene();
    }


    public void settingForm(Service service, Stage stage) {
        this.service = service;
        stage.setTitle(String.format(rb.getString("FORM_TITLE"), service.getServName()));
        fillingTable();
        for(CategoryOfSupplies categoryOfSupplies: service.getCategoriesOfSupplies()){
            System.out.println(categoryOfSupplies);
        }
    }

    private void fillingTable(){
        try {
            List<CategoryOfSupplies> categoriesOfSupplies = categoryOfSuppliesRepository.getAll();
            if(!categoriesOfSupplies.isEmpty()) {
                for (CategoryOfSupplies categoryOfSupplies : categoriesOfSupplies) {
                    CheckBox check = new CheckBox();
                    if (service.getCategoriesOfSupplies().stream().anyMatch(c->c.getCsupName().equals(categoryOfSupplies.getCsupName()))) {
                        check.setSelected(true);
                    }
                    CategoryOfSuppliesForServiceFX category = new CategoryOfSuppliesForServiceFX(check, categoryOfSupplies.getCsupName());
                    suppliesForServiceFXES.add(category);
                }

                suppliesForServiceTable.setItems(suppliesForServiceFXES);
                suppliesForServiceTable.getSelectionModel().selectFirst();
                Platform.runLater(() -> suppliesForServiceTable.requestFocus());
            }else{
                btSave.setDisable(true);
            }


        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            suppliesForServiceTable.requestFocus();
        }
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        try {
            Service actualService = serviceRepository.get(service.getServName());
            if(actualService != null){
                service.setCategoriesOfSupplies(actualService.getCategoriesOfSupplies());
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

        suppliesForServiceFXES.clear();
        fillingTable();
    }

    public void btSaveAction(ActionEvent actionEvent) {
        List<CategoryOfSupplies> necessaryCategories = new ArrayList<>();
        for(CategoryOfSuppliesForServiceFX category: suppliesForServiceFXES){
            if(category.getSelect().isSelected()){
                necessaryCategories.add(new CategoryOfSupplies(category.getCsupName()));
            }
        }
        try {
            Service editedService = serviceRepository.editCategoriesOfSupplies(new Service(service.getServName(), necessaryCategories));
            if(editedService != null){
                service.setCategoriesOfSupplies(editedService.getCategoriesOfSupplies());
                FXHelper.showInfoAlert(String.format(rb.getString("SUCCESS_SAVE"), service.getServName()));
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

        suppliesForServiceTable.requestFocus();
    }
}
