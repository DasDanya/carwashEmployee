package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.Users.UserRole;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Контроллер формы с категориями расходных материалов
 */
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
    private TableView<CategoriesOfSuppliesFX> categoriesTable;
    @FXML
    private TableColumn<CategoriesOfSuppliesFX, String> categoryNameColumn;
    @FXML
    private TableColumn<CategoriesOfSuppliesFX, String> unitColumn;

    private ResourceBundle rb;

    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();
    private ObservableList<CategoriesOfSuppliesFX> categoriesOfSuppliesFXES = FXCollections.observableArrayList();


    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryNameColumn.setCellValueFactory(c->c.getValue().csupNameProperty());
        unitColumn.setCellValueFactory(c->c.getValue().unitProperty());
        categoriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        fillingAll();
        settingTooltipForButtons();
        FXHelper.setContextMenuForEditableTextField(searchField);
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    /**
     * Заполняет таблицу категорий всеми категориями расходных материалов
     */
    private void fillingAll() {
        try{
            List<CategoryOfSupplies> categories = categoryOfSuppliesRepository.getAll();
            fillingObservableList(categories);
            categoriesTable.setItems(categoriesOfSuppliesFXES);

            categoriesTable.getSelectionModel().selectFirst();
            Platform.runLater(()->categoriesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            categoriesTable.requestFocus();
        }
    }

    /**
     * Заполняет ObservableList данными о категориях расходных материалов
     *
     * @param categories список категорий для заполнения
     */
    private void fillingObservableList(List<CategoryOfSupplies> categories){
        categoriesOfSuppliesFXES.clear();
        for(CategoryOfSupplies category: categories){
            CategoriesOfSuppliesFX categoriesOfSuppliesFX = new CategoriesOfSuppliesFX(category.getCsupName(), category.getUnit());
            categoriesOfSuppliesFXES.add(categoriesOfSuppliesFX);
        }
    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
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

    /**
     * Возвращает текущую сцену (Scene).
     *
     * @return текущая сцена
     */
    private Scene getActualScene(){
        return categoriesTable.getScene();
    }

    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    /**
     * Выполняет операции с категорией расходных материалов
     * @param operationMode Режим операции
     */
    private void doOperation(FXOperationMode operationMode){
        if(!AppHelper.getUserInfo().get(2).equals(UserRole.OWNER.name())){
            FXHelper.showErrorAlert(AppHelper.getCannotAccessOperationText());
            categoriesTable.requestFocus();
        }else {
            if (operationMode != FXOperationMode.EDIT) {
                CategoryOfSupplies categoryOfSupplies = null;
                CategoriesOfSuppliesFX selectedCategoryOfSuppliesFX = null;
                switch (operationMode) {
                    case CREATE:
                        categoryOfSupplies = new CategoryOfSupplies();
                        break;
                    case DELETE:
                        if (categoriesTable.getSelectionModel().getSelectedItem() != null) {
                            selectedCategoryOfSuppliesFX = categoriesTable.getSelectionModel().getSelectedItem();
                            categoryOfSupplies = new CategoryOfSupplies();
                            categoryOfSupplies.setCsupName(selectedCategoryOfSuppliesFX.getCsupName());
                            categoryOfSupplies.setUnit(UnitOfMeasure.valueOfDisplayValue(selectedCategoryOfSuppliesFX.getUnit()));
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
                        doResult(operationMode, editCategoryOfSuppliesController.getExitMode(), categoryOfSupplies, selectedCategoryOfSuppliesFX);

                    } catch (Exception e) {
                        FXHelper.showErrorAlert(e.getMessage());
                        categoriesTable.requestFocus();
                    }
                }
            }
        }
    }

    /**
     * Обрабатывает результат выполнения операции.
     *
     * @param operationMode режим операции
     * @param exitMode режим выхода из формы
     * @param categoryOfSupplies объект категории расходных материалов
     * @param selectedCategory выбранная CategoriesOfSuppliesFX для удаления
     */
    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, CategoryOfSupplies categoryOfSupplies, CategoriesOfSuppliesFX selectedCategory){
        if(exitMode == FXFormExitMode.OK){
            if(operationMode == FXOperationMode.CREATE){
                CategoriesOfSuppliesFX newCategory = new CategoriesOfSuppliesFX(categoryOfSupplies.getCsupName(), categoryOfSupplies.getUnit());
                categoriesOfSuppliesFXES.add(newCategory);
                categoriesOfSuppliesFXES.sort(Comparator.comparing(CategoriesOfSuppliesFX::getCsupName, String::compareToIgnoreCase));

                categoriesTable.getSelectionModel().select(newCategory);
                categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
            }else{
                categoriesOfSuppliesFXES.remove(selectedCategory);
            }
        }
        categoriesTable.requestFocus();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }


    /**
     * Выполняет обновление данных.
     */
    private void doRefresh(){
        categoriesOfSuppliesFXES.clear();
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
                fillingObservableList(resultOfSearch);
                if(!categoriesOfSuppliesFXES.isEmpty()){
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
