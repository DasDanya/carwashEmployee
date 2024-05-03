package ru.pin120.carwashemployee.CategoriesOfCars;

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
import ru.pin120.carwashemployee.CategoriesAndServices.CategoryOfServicesFX;
import ru.pin120.carwashemployee.CategoriesAndServices.EditCategoryOfServicesController;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class CategoriesOfCarsController implements Initializable {

    @FXML
    private TableView<CategoryOfCarsFX> categoriesTable;
    @FXML
    private TableColumn<CategoryOfCarsFX, String> categoryNameColumn;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private Button refreshButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button createButton;
    private ResourceBundle rb;

    private ObservableList<CategoryOfCarsFX> categoryOfCarsFXES = FXCollections.observableArrayList();
    private CategoryOfCarsRepository categoryOfCarsRepository = new CategoryOfCarsRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryNameColumn.prefWidthProperty().bind(categoriesTable.widthProperty());
        categoryNameColumn.setCellValueFactory(c -> c.getValue().catCarsNameProperty());
        fillingAll();

        categoriesTable.getSelectionModel().selectFirst();
        Platform.runLater(() -> categoriesTable.requestFocus());

        settingTooltipForButtons();
    }

    private void fillingAll(){
        try {
            List<CategoryOfCars> categoryOfCars = categoryOfCarsRepository.getAll();
            fillingObservableList(categoryOfCars);
            categoriesTable.setItems(categoryOfCarsFXES);
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<CategoryOfCars> categoryOfCars){
        for(CategoryOfCars category: categoryOfCars){
            CategoryOfCarsFX categoryOfCarsFX = new CategoryOfCarsFX(category.getCatCarsName());
            categoryOfCarsFXES.add(categoryOfCarsFX);
        }
    }

    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_CATEGORY")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_CATEGORY")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
//        bindButton.setOnMouseEntered(event -> {
//            bindButton.setTooltip(new Tooltip(rb.getString("BIND_CATEGORY")));
//        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
    }

    private Scene getActualScene(){ return categoriesTable.getScene();}


    private void doOperation(FXOperationMode operationMode){
        CategoryOfCars categoryOfCars = null;
        CategoryOfCarsFX categoryOfCarsFX = null;

        switch (operationMode){
            case CREATE:
                categoryOfCars = new CategoryOfCars();
                break;
            case DELETE:
                if(categoriesTable.getSelectionModel().getSelectedItem() != null){
                    categoryOfCars = new CategoryOfCars();
                    categoryOfCarsFX = categoriesTable.getSelectionModel().getSelectedItem();
                    categoryOfCars.setCatCarsName(categoryOfCarsFX.getCatCarsName());
                }
                break;
        }
        if (categoryOfCars == null) {
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
            categoriesTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesOfCars.resources.EditCategoryOfCars", "CategoriesOfCars/fxml/EditCategoryOfCars.fxml", getActualScene());
                EditCategoryOfCarsController editCategoryOfCarsController = fxWindowData.getLoader().getController();

                editCategoryOfCarsController.setParameters(categoryOfCars, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
                doResult(operationMode, editCategoryOfCarsController.getExitMode(), categoryOfCars, categoryOfCarsFX);

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                categoriesTable.requestFocus();
            }
        }


    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfCars categoryOfCars, CategoryOfCarsFX categoryOfCarsFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    categoryOfCarsFXES.add(new CategoryOfCarsFX(categoryOfCars.getCatCarsName()));
                    ObservableList<CategoryOfCarsFX> sortedCategories = FXCollections.observableArrayList(categoryOfCarsFXES);
                    sortedCategories.sort(Comparator.comparing(CategoryOfCarsFX::getCatCarsName, String::compareToIgnoreCase));
                    categoryOfCarsFXES.setAll(sortedCategories);
                    categoriesTable.setItems(categoryOfCarsFXES);
                    break;
                case DELETE:
                    categoryOfCarsFXES.remove(categoryOfCarsFX);
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
    }

    private void doRefresh(){
        categoryOfCarsFXES.clear();
        searchField.clear();
        fillingAll();

        categoriesTable.getSelectionModel().selectFirst();
        categoriesTable.requestFocus();

        categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
    }
    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        doSearch();
    }

    private void doSearch(){
        if(searchField.getText() == null || searchField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("SEARCH_FIELD_IS_EMPTY"));
            searchField.requestFocus();
        }else{
            categoryOfCarsFXES.clear();
            String parameter = searchField.getText().trim();
            try{
                List<CategoryOfCars> categoryOfCars = categoryOfCarsRepository.getCategoriesOfCarsByCarCarsName(parameter);
                fillingObservableList(categoryOfCars);

                if(!categoryOfCarsFXES.isEmpty()){
                    categoriesTable.requestFocus();
                    categoriesTable.getSelectionModel().selectFirst();
                }else{
                    searchField.requestFocus();
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                categoriesTable.requestFocus();
            }
        }
    }
}
