package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportFX;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesOfSuppliesController implements Initializable {

    @FXML
    private Button createButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button refreshButton;
    @FXML
    private Button searchButton;
    @FXML
    private TextField searchField;
    @FXML
    private TableView<CategoryOfSupplies> categoriesTable;
    @FXML
    private TableColumn<CategoryOfSupplies, String> categoryNameColumn;
    private ResourceBundle rb;

    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();
    private ObservableList<CategoryOfSupplies> categoriesOfSupplies = FXCollections.observableArrayList();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryNameColumn.prefWidthProperty().bind(categoriesTable.widthProperty());
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<CategoryOfSupplies, String>("csupName"));

        fillingAll();
        settingTooltipForButtons();
        FXHelper.setContextMenuForEditableTextField(searchField);
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    private void fillingAll() {
        try{
            List<CategoryOfSupplies> categories = categoryOfSuppliesRepository.getAll();
            categoriesOfSupplies.setAll(categories);
            categoriesTable.setItems(categoriesOfSupplies);

            categoriesTable.getSelectionModel().selectFirst();
            Platform.runLater(()->categoriesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            categoriesTable.requestFocus();
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
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
    }

    private Scene getActualScene(){
        return categoriesTable.getScene();
    }

    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    private void doOperation(FXOperationMode operationMode){
        if(operationMode != FXOperationMode.EDIT) {
            CategoryOfSupplies categoryOfSupplies = null;
            switch (operationMode) {
                case CREATE:
                    categoryOfSupplies = new CategoryOfSupplies();
                    break;
                case DELETE:
                    if (categoriesTable.getSelectionModel().getSelectedItem() != null) {
                        categoryOfSupplies = categoriesTable.getSelectionModel().getSelectedItem();
                    }
                    break;
            }
            if (categoryOfSupplies == null) {
                FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
                categoriesTable.requestFocus();
            } else {
                try {
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesOfSupplies.resources.EditCategoryOfSupplies", "CategoriesOfSupplies/fxml/EditCategoryOfSupplies.fxml", getActualScene());
                    EditCategoryOfSuppliesController editCategoryOfSuppliesController = fxWindowData.getLoader().getController();

                    editCategoryOfSuppliesController.setParameters(categoryOfSupplies, operationMode, fxWindowData.getModalStage());
                    fxWindowData.getModalStage().showAndWait();
                    doResult(operationMode, editCategoryOfSuppliesController.getExitMode(), categoryOfSupplies);

                } catch (Exception e) {
                    FXHelper.showErrorAlert(e.getMessage());
                    categoriesTable.requestFocus();
                }
            }
        }
    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfSupplies categoryOfSupplies){
        if(exitMode == FXFormExitMode.OK){
            if(operationMode == FXOperationMode.CREATE){
                categoriesOfSupplies.add(categoryOfSupplies);
                categoriesOfSupplies.sort(Comparator.comparing(CategoryOfSupplies::getCsupName, String::compareToIgnoreCase));

                categoriesTable.getSelectionModel().select(categoryOfSupplies);
                categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
            }else{
                categoriesOfSupplies.remove(categoryOfSupplies);
            }
        }
        categoriesTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        categoriesOfSupplies.clear();
        searchField.clear();

        fillingAll();
        categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);

    }
    public void searchButtonAction(ActionEvent actionEvent) {
        if(searchField.getText() == null || searchField.getText().isBlank()){
            FXHelper.showErrorAlert(rb.getString("SEARCH_FIELD_IS_EMPTY"));
            searchField.requestFocus();
        }else{
            String csupName = searchField.getText().trim();
            try{
                List<CategoryOfSupplies> resultOfSearch = categoryOfSuppliesRepository.search(csupName);
                categoriesOfSupplies.setAll(resultOfSearch);

                if(!categoriesOfSupplies.isEmpty()){
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
