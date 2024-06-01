package ru.pin120.carwashemployee.Supplies;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoriesOfSuppliesFX;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSuppliesRepository;
import ru.pin120.carwashemployee.CategoriesOfSupplies.UnitOfMeasure;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class EditSupplyController implements Initializable {
    @FXML
    private Label measureLabel;
    @FXML
    private TextField searchField;
    @FXML
    private TextField photoInfoField;
    @FXML
    private TextField nameField;
    @FXML
    private TableView<CategoriesOfSuppliesFX> categoriesTable;
    @FXML
    private TableColumn<CategoriesOfSuppliesFX, String> categoryNameColumn;
    @FXML
    private TableColumn<CategoriesOfSuppliesFX, String> unitColumn;
    @FXML
    private Spinner<Integer> countSpinner;
    @FXML
    private Spinner<Integer> measureSpinner;

    @FXML
    private Button searchButton;
    @FXML
    private Button loadImageButton;
    @FXML
    private Button showImageButton;
    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private Button refreshButton;
    private ResourceBundle rb;
    private Supply supply;
    @FXML
    private Stage stage;
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    private SupplyRepository supplyRepository = new SupplyRepository();
    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();
    private ObservableList<CategoriesOfSuppliesFX> categoriesOfSuppliesFXES = FXCollections.observableArrayList();

    private File selectedPhoto;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        categoryNameColumn.setCellValueFactory(c->c.getValue().csupNameProperty());
        unitColumn.setCellValueFactory(c->c.getValue().unitProperty());
        categoriesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        fillingAll();

        FXHelper.setContextMenuForEditableTextField(measureSpinner.getEditor());
        FXHelper.setContextMenuForEditableTextField(countSpinner.getEditor());
        FXHelper.setContextMenuForEditableTextField(nameField);

        nameFieldListener();
        tableSelectModelListener();

        settingTooltipForButtons();
    }

    private void tableSelectModelListener() {
        categoriesTable.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->  {
            setMeasureLabelText(newValue);
        });
    }

    private void fillingAll() {
        try{
            List<CategoryOfSupplies> categories = categoryOfSuppliesRepository.getAll();
            fillingObservableList(categories);
            categoriesTable.setItems(categoriesOfSuppliesFXES);

            categoriesTable.getSelectionModel().selectFirst();
            Platform.runLater(()->categoriesTable.requestFocus());
            btOK.setDisable(categoriesOfSuppliesFXES.isEmpty());
            setMeasureLabelText(categoriesTable.getSelectionModel().getSelectedItem());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
            categoriesTable.requestFocus();
        }
    }


    private void setMeasureLabelText(CategoriesOfSuppliesFX category){
        if(category != null){
            measureLabel.setText(category.getUnit().equals(UnitOfMeasure.MILLILITERS.getDisplayValue()) ? rb.getString("AMOUNT_UNIT") : rb.getString("COUNT_UNIT"));
        }else{
            measureLabel.setText(rb.getString("MEASURE"));
        }
    }

    private void fillingObservableList(List<CategoryOfSupplies> categories){
        categoriesOfSuppliesFXES.clear();
        for(CategoryOfSupplies category: categories){
            CategoriesOfSuppliesFX categoriesOfSuppliesFX = new CategoriesOfSuppliesFX(category.getCsupName(), category.getUnit());
            categoriesOfSuppliesFXES.add(categoriesOfSuppliesFX);
        }
    }

    private void settingTooltipForButtons() {
        loadImageButton.setOnMouseEntered(event -> {
            loadImageButton.setTooltip(new Tooltip(rb.getString("LOAD_PHOTO")));
        });
        showImageButton.setOnMouseEntered(event -> {
            showImageButton.setTooltip(new Tooltip(rb.getString("SHOW_PHOTO")));
        });
        searchButton.setOnMouseEntered(event -> {
            searchButton.setTooltip(new Tooltip(rb.getString("SEARCH_CATEGORY")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
    }



    private void settingSpinners() {
        int minCountSpinnerValue = operationMode == FXOperationMode.CREATE ? 1 : 0;
        countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(minCountSpinnerValue, Integer.MAX_VALUE,1,1));
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                return change;
            }
            return null;
        });

        countSpinner.getEditor().setTextFormatter(formatter);

        measureSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE,1,1));
        TextFormatter<Integer> formatter2 = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                return change;
            }
            return null;
        });

        measureSpinner.getEditor().setTextFormatter(formatter2);
    }


    private void nameFieldListener() {
        nameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > SupplyFX.MAX_LENGTH_NAME) {
                    nameField.setText(oldValue);
                }
            }
        });
    }

    public void setParameters(Supply supply, FXOperationMode operationMode, Stage modalStage) {
        this.supply = supply;
        this.operationMode = operationMode;
        this.stage = modalStage;
        settingSpinners();

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                photoInfoField.setText(rb.getString("DEFAULT_PHOTO"));
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE"));
                photoInfoField.setText(rb.getString("SUPPLY_PHOTO"));
                measureSpinner.setDisable(true);
                nameField.setEditable(false);
                searchField.setEditable(false);
                searchButton.setDisable(true);
                categoriesTable.setDisable(true);

                fillingComponents();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE"));
                photoInfoField.setText(rb.getString("SUPPLY_PHOTO"));
                measureSpinner.setDisable(true);
                countSpinner.setDisable(true);
                nameField.setEditable(false);
                loadImageButton.setDisable(true);
                searchField.setEditable(false);
                searchButton.setDisable(true);
                categoriesTable.setDisable(true);

                fillingComponents();
                Platform.runLater(()->btOK.requestFocus());
                break;
        }

        closeWindowAction();
    }

    private void fillingComponents(){
        nameField.setText(supply.getSupName());
        countSpinner.getValueFactory().setValue(supply.getSupCount());
        measureSpinner.getValueFactory().setValue(supply.getSupMeasure());


        searchField.setText(supply.getCategory().getCsupName());
        doSearch();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    public void searchButtonAction(ActionEvent actionEvent) {
        doSearch();
    }

    private void doSearch(){
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

    public void loadImageButtonAction(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("SELECT_PHOTO"));

        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("IMAGES", "*.png", "*.jpg", "*.jpeg", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png"),
                new FileChooser.ExtensionFilter("JPEG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp")
        );

        selectedPhoto = fileChooser.showOpenDialog(stage);
        if(selectedPhoto != null){
            photoInfoField.setText(rb.getString("NEW_PHOTO") + " " + selectedPhoto.getAbsolutePath());
        }
    }

    public void showImageButtonAction(ActionEvent actionEvent) {
        try {
            FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Supplies.resources.ShowSupplyPhoto", "Supplies/fxml/ShowSupplyPhoto.fxml", getActualScene());
            ShowSupplyPhotoController showSupplyPhotoController = fxWindowData.getLoader().getController();
            if(operationMode == FXOperationMode.CREATE || ((operationMode == FXOperationMode.EDIT || operationMode == FXOperationMode.DELETE) && selectedPhoto != null)) {
                if(operationMode == FXOperationMode.CREATE) {
                    showSupplyPhotoController.showPhoto(FXHelper.convertToImage(selectedPhoto));
                }else{
                    showSupplyPhotoController.showPhoto(FXHelper.convertToImage(selectedPhoto), supply);
                }
            }else{
                showSupplyPhotoController.showPhoto(supply);
            }
            fxWindowData.getModalStage().showAndWait();
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    private Scene getActualScene(){
        return categoriesTable.getScene();
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        CategoriesOfSuppliesFX category = categoriesTable.getSelectionModel().getSelectedItem();
        if(nameField.getText() == null || nameField.getText().isBlank()) {
            nameField.clear();
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_NAME"));
            nameField.requestFocus();
        } else if(category == null){
            FXHelper.showErrorAlert(rb.getString("NOT_EMPTY_CATEGORY"));
            searchField.requestFocus();
        }else{
            try {
                switch (operationMode) {
                    case CREATE:
                        supply.setSupName(nameField.getText().trim());
                        supply.setSupCount(countSpinner.getValue());
                        supply.setSupMeasure(measureSpinner.getValue());
                        CategoryOfSupplies categoryOfSupplies = new CategoryOfSupplies(category.getCsupName(), UnitOfMeasure.valueOfDisplayValue(category.getUnit()));
                        supply.setCategory(categoryOfSupplies);

                        Supply createdSupply = supplyRepository.create(supply, selectedPhoto);
                        if(createdSupply != null){
                            canExit = true;
                            supply.setSupId(createdSupply.getSupId());
                            supply.setSupPhotoName(createdSupply.getSupPhotoName());
                        }
                        break;
                    case EDIT:
                        if(supply.getSupName().equals(nameField.getText().trim()) && supply.getSupCount() == countSpinner.getValue() && supply.getSupMeasure() == measureSpinner.getValue() &&
                        supply.getCategory().getCsupName().equals(category.getCsupName()) && photoInfoField.getText().equals(rb.getString("SUPPLY_PHOTO"))){
                            canExit = true;
                        }else{
                            supply.setSupName(nameField.getText().trim());
                            supply.setSupCount(countSpinner.getValue());
                            supply.setSupMeasure(measureSpinner.getValue());
                            CategoryOfSupplies categoryOfSuppliesEd = new CategoryOfSupplies(category.getCsupName(), UnitOfMeasure.valueOfDisplayValue(category.getUnit()));
                            supply.setCategory(categoryOfSuppliesEd);

                            Supply editedSupply = supplyRepository.edit(supply, selectedPhoto);
                            if(editedSupply != null){
                                supply.setSupPhotoName(supply.getSupPhotoName());
                                canExit = true;
                            }
                        }
                        break;
                    case DELETE:
                        canExit = supplyRepository.delete(supply.getSupId());
                        break;
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
            if(canExit){
                exitMode = FXFormExitMode.OK;
                stage.close();
            }else{
                if(operationMode == FXOperationMode.DELETE){
                    btCancel.requestFocus();
                }
            }
        }
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    private void doRefresh(){
        categoriesOfSuppliesFXES.clear();
        searchField.clear();

        fillingAll();
        categoryNameColumn.setSortType(TableColumn.SortType.ASCENDING);
    }
}
