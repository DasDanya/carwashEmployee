package ru.pin120.carwashemployee.CategoriesOfServices;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesAndServices.*;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.*;

public class CategoriesOfServicesController implements Initializable {

    private ResourceBundle rb;

    @FXML
    private TableView<CategoryOfServicesFX> categoriesTable;
    @FXML
    private TableColumn<CategoryOfServicesFX, String> categoryNameColumn;

    @FXML
    private Button searchButton;
    @FXML
    private Button createButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button bindButton;
    @FXML
    private Button refreshButton;

    @FXML
    private TextField searchField;

    private ObservableList<CategoryOfServicesFX> categoriesOfServices = FXCollections.observableArrayList();

    //private List<String> categoriesNames;

    private CategoriesOfServicesRepository categoriesOfServicesRepository = new CategoriesOfServicesRepository();
    private ServiceRepository serviceRepository = new ServiceRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;


        categoryNameColumn.prefWidthProperty().bind(categoriesTable.widthProperty());
        categoryNameColumn.setCellValueFactory(c -> c.getValue().nameProperty());

        getAllCategories();
        categoriesTable.setItems(categoriesOfServices);

        categoriesTable.getSelectionModel().selectFirst();
        Platform.runLater(() -> categoriesTable.requestFocus());

        settingTooltipForButtons();
        Platform.runLater(() ->FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doBind, this::doRefresh));

    }

    private void getAllCategories(){
        try {
            List<String> categoriesNames = categoriesOfServicesRepository.getCategoriesName();
            for (String categoriesName : categoriesNames) {
                categoriesOfServices.add(new CategoryOfServicesFX(categoriesName));
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_CATEGORY")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_CATEGORY")));
        });
        bindButton.setOnMouseEntered(event -> {
            bindButton.setTooltip(new Tooltip(rb.getString("BIND")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
    }

    private Scene getActualScene(){
        return categoriesTable.getScene();
    }

    private void doOperation(FXOperationMode operationMode){
        if(operationMode != FXOperationMode.EDIT) {
            CategoryOfServices categoryOfServices = null;
            CategoryOfServicesFX selectedCategoryOfServicesFX = null;
            switch (operationMode) {
                case CREATE:
                    categoryOfServices = new CategoryOfServices();
                    break;
                case DELETE:
                    if (categoriesTable.getSelectionModel().getSelectedItem() != null) {
                        categoryOfServices = new CategoryOfServices();
                        categoryOfServices.setCatName(categoriesTable.getSelectionModel().getSelectedItem().getName());
                        selectedCategoryOfServicesFX = categoriesTable.getSelectionModel().getSelectedItem();
                    }
                    break;
            }
            if (categoryOfServices == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
                categoriesTable.requestFocus();
            } else {
                try {
                    Locale locale = Locale.getDefault();
                    ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.CategoriesOfServices.resources.EditCategoryOfServices", locale);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/EditCategoryOfServices.fxml"), bundle);
                    Parent root = loader.load();

                    Scene modalScene = new Scene(root);
                    Stage modalStage = new Stage();
                    modalStage.setScene(modalScene);
                    modalStage.initModality(Modality.WINDOW_MODAL);
                    modalStage.initOwner(getActualScene().getWindow());
                    modalStage.getIcons().add(AppHelper.getMainIcon());

                    EditCategoryOfServicesController categoryOfServicesController = loader.getController();
                    categoryOfServicesController.setParameters(categoryOfServices, operationMode, modalStage);

                    modalStage.showAndWait();
                    doResultCategoryOfServices(operationMode, categoryOfServicesController.getExitMode(), categoryOfServices, selectedCategoryOfServicesFX);


                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    categoriesTable.requestFocus();
                }
            }
        }
    }

    private void doResultCategoryOfServices(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfServices categoryOfServices, CategoryOfServicesFX selectedCategoryOfServicesFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    categoryOfServices.setServices(new ArrayList<>()); // чтобы не ругался на пустой список
                    categoriesOfServices.add(new CategoryOfServicesFX(categoryOfServices.getCatName()));
                    // сортировка элементов в таблице
                    ObservableList<CategoryOfServicesFX> sortedCategories = FXCollections.observableArrayList(categoriesOfServices);
                    sortedCategories.sort(Comparator.comparing(CategoryOfServicesFX::getName, String::compareToIgnoreCase));
                    categoriesOfServices.setAll(sortedCategories);
                    categoriesTable.setItems(categoriesOfServices);
                    break;
                case DELETE:
                    categoriesOfServices.remove(selectedCategoryOfServicesFX);
                    break;
            }
        }

        categoriesTable.requestFocus();
    }

    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    public void bindButtonAction(ActionEvent actionEvent) {
        doBind();
    }

    private void doBind(){
        if(!categoriesTable.getItems().isEmpty()) {
            if(categoriesTable.getSelectionModel().getSelectedItem() != null) {
                try {
                    String selectedCatName = categoriesTable.getSelectionModel().getSelectedItem().getName();
                    List<Service> servicesOfCategory = serviceRepository.getServicesByCatName(selectedCatName);
                    if(!servicesOfCategory.isEmpty()) {
                        int indexSelectedCategory = categoriesTable.getSelectionModel().getSelectedIndex();
                        Locale locale = Locale.getDefault();
                        ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.CategoriesOfServices.resources.BindWithCategory", locale);
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("fxml/BindWithCategory.fxml"), bundle);
                        Parent root = loader.load();

                        Scene modalScene = new Scene(root);
                        Stage modalStage = new Stage();
                        modalStage.setScene(modalScene);
                        modalStage.initModality(Modality.WINDOW_MODAL);
                        modalStage.initOwner(getActualScene().getWindow());
                        modalStage.getIcons().add(AppHelper.getMainIcon());

                        BindWithCategoryController controller = loader.getController();
                        //controller.setParameters(modalStage, BindWithCategoryMode.CATEGORY, selectedCatName, selectedCatName);
                        modalStage.showAndWait();

                        if(controller.getExitMode() == FXFormExitMode.OK){
                            FXHelper.showInfoAlert(rb.getString("BIND_CATEGORY_SUCCESS") + " " + selectedCatName);
                        }
                        categoriesTable.getSelectionModel().select(indexSelectedCategory);
                    }else{
                        FXHelper.showErrorAlert(rb.getString("NOT_EXISTS_SERVICES_FOR_BIND"));
                    }
                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    categoriesTable.requestFocus();
                }
            }else{
                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
            }
        }else{
            FXHelper.showErrorAlert(rb.getString("NOT_EXISTS_CATEGORY_FOR_BIND"));
        }

        categoriesTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        categoriesOfServices.clear();
        getAllCategories();

        if(!categoriesTable.getItems().isEmpty()){
            categoriesTable.getSelectionModel().selectFirst();
        }

        searchField.clear();
    }

    public void searchButtonAction(ActionEvent actionEvent) {

    }


}
