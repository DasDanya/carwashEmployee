package ru.pin120.carwashemployee.CategoriesOfTransport;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class CategoriesOfTransportController implements Initializable {

    @FXML
    private TableView<CategoryOfTransportFX> categoriesTable;
    @FXML
    private TableColumn<CategoryOfTransportFX, String> categoryNameColumn;
    @FXML
    private TableColumn<CategoryOfTransportFX, Long> categoryIdColumn;
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
    @FXML
    private Button editButton;
    private ResourceBundle rb;

    private ObservableList<CategoryOfTransportFX> categoryOfTransportFXES = FXCollections.observableArrayList();
    private CategoryOfTransportRepository categoryOfTransportRepository = new CategoryOfTransportRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryNameColumn.prefWidthProperty().bind(categoriesTable.widthProperty());
        categoryNameColumn.setCellValueFactory(c -> c.getValue().catTrNameProperty());
        categoryIdColumn.setCellValueFactory(c->c.getValue().catTrIdProperty().asObject());
        fillingAll();

        categoriesTable.getSelectionModel().selectFirst();
        Platform.runLater(() -> categoriesTable.requestFocus());

        settingTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    private void fillingAll(){
        try {
            List<CategoryOfTransport> categoryOfCars = categoryOfTransportRepository.getAll();
            fillingObservableList(categoryOfCars);
            categoriesTable.setItems(categoryOfTransportFXES);
        } catch (Exception e) {
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private void fillingObservableList(List<CategoryOfTransport> categoryOfCars){
        for(CategoryOfTransport category: categoryOfCars){
            CategoryOfTransportFX categoryOfTransportFX = new CategoryOfTransportFX(category.getCatTrName(),category.getCatTrId());
            categoryOfTransportFXES.add(categoryOfTransportFX);
        }
    }

    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_CATEGORY")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_CATEGORY")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_CATEGORY")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        searchButton.setOnMouseEntered(event->{
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
    }

    private Scene getActualScene(){ return categoriesTable.getScene();}


    private void doOperation(FXOperationMode operationMode){
        CategoryOfTransport categoryOfTransport = null;
        CategoryOfTransportFX categoryOfTransportFX = null;

        switch (operationMode){
            case CREATE:
                categoryOfTransport = new CategoryOfTransport();
                break;
            case EDIT:
            case DELETE:
                if(categoriesTable.getSelectionModel().getSelectedItem() != null){
                    categoryOfTransport = new CategoryOfTransport();
                    categoryOfTransportFX = categoriesTable.getSelectionModel().getSelectedItem();
                    categoryOfTransport.setCatTrName(categoryOfTransportFX.getCatTrName());
                    categoryOfTransport.setCatTrId(categoryOfTransportFX.getCatTrId());
                }
                break;
        }
        if (categoryOfTransport == null) {
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_CATEGORY"));
            categoriesTable.requestFocus();
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.CategoriesOfTransport.resources.EditCategoryOfTransport", "CategoriesOfTransport/fxml/EditCategoryOfTransport.fxml", getActualScene());
                EditCategoryOfTransportController editCategoryOfTransportController = fxWindowData.getLoader().getController();

                editCategoryOfTransportController.setParameters(categoryOfTransport, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
                doResult(operationMode, editCategoryOfTransportController.getExitMode(), categoryOfTransport, categoryOfTransportFX);

            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                categoriesTable.requestFocus();
            }
        }


    }

    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfTransport categoryOfTransport, CategoryOfTransportFX categoryOfTransportFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    CategoryOfTransportFX category = new CategoryOfTransportFX(categoryOfTransport.getCatTrName(), categoryOfTransport.getCatTrId());
                    categoryOfTransportFXES.add(category);
                    categoryOfTransportFXES.sort(Comparator.comparing(CategoryOfTransportFX::getCatTrName, String::compareToIgnoreCase));

                    categoriesTable.getSelectionModel().select(category);
                    categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    categoryOfTransportFXES.remove(categoryOfTransportFX);
                    break;
                case EDIT:
                    categoryOfTransportFX.setCatTrId(categoryOfTransport.getCatTrId());
                    categoryOfTransportFX.setCatTrName(categoryOfTransport.getCatTrName());

                    categoryOfTransportFXES.sort(Comparator.comparing(CategoryOfTransportFX::getCatTrName, String::compareToIgnoreCase));
                    categoriesTable.getSelectionModel().select(categoryOfTransportFX);
                    categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
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

    public void editButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.EDIT);
    }

    private void doRefresh(){
        categoryOfTransportFXES.clear();
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
            String parameter = searchField.getText().trim();
            try{
                List<CategoryOfTransport> categoryOfCars = categoryOfTransportRepository.getCategoriesOfTransportByCatTrName(parameter);
                categoryOfTransportFXES.clear();
                fillingObservableList(categoryOfCars);

                if(!categoryOfTransportFXES.isEmpty()){
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