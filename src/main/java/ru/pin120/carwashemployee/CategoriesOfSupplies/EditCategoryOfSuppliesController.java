package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportFX;
import ru.pin120.carwashemployee.Cleaners.CleanerStatus;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategoryOfSuppliesController implements Initializable {

    private static final int MAX_LENGTH_CATEGORY_NAME = 30;
    private static final String CATEGORY_REGEX = "^[а-яА-ЯёЁ -]+$";

    @FXML
    private ComboBox<UnitOfMeasure> unitComboBox;
    @FXML
    private Button btCancel;
    @FXML
    private TextField categoryNameField;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    private FXOperationMode operationMode;
    @Getter
    private FXFormExitMode exitMode;
    private CategoryOfSupplies categoryOfSupplies;
    private CategoryOfSuppliesRepository categoryOfSuppliesRepository = new CategoryOfSuppliesRepository();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForEditableTextField(categoryNameField);
        categoryNameFieldTextListener();
        setConvertersForComboBoxes();
    }

    private void setConvertersForComboBoxes() {
        unitComboBox.getItems().setAll(UnitOfMeasure.values());
        unitComboBox.setConverter(new StringConverter<UnitOfMeasure>() {
            @Override
            public String toString(UnitOfMeasure unit) {
                return unit.getDisplayValue();
            }

            @Override
            public UnitOfMeasure fromString(String string) {
                return null;
            }
        });
    }

    private void categoryNameFieldTextListener() {
        categoryNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > MAX_LENGTH_CATEGORY_NAME) {
                    categoryNameField.setText(oldValue);
                }
            }
        });
    }

    public void setParameters(CategoryOfSupplies categoryOfSupplies, FXOperationMode operationMode, Stage modalStage) {
        this.categoryOfSupplies = categoryOfSupplies;
        this.operationMode = operationMode;
        stage = modalStage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_FORM_TITLE"));
                unitComboBox.getSelectionModel().selectFirst();
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_FORM_TITLE"));
                unitComboBox.getSelectionModel().select(categoryOfSupplies.getUnit());
                categoryNameField.setDisable(true);
                unitComboBox.setDisable(true);
                break;

        }

        categoryNameField.setText(categoryOfSupplies.getCsupName());
        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(categoryNameField.getText() == null || categoryNameField.getText().isBlank()) {
            FXHelper.showErrorAlert(rb.getString("EMPTY_CATEGORY_NAME"));
            categoryNameField.clear();
            categoryNameField.requestFocus();
        }else if(!categoryNameField.getText().matches(CATEGORY_REGEX)){
            FXHelper.showErrorAlert(rb.getString("CATEGORY_NAME_VALID_CHARACTERS"));
            categoryNameField.requestFocus();
        }else{
            try{
                if(operationMode == FXOperationMode.CREATE){
                    categoryOfSupplies.setCsupName(categoryNameField.getText().trim());
                    categoryOfSupplies.setUnit(unitComboBox.getSelectionModel().getSelectedItem());
                    if(categoryOfSuppliesRepository.create(categoryOfSupplies) != null){
                        canExit = true;
                    }
                }else{
                    canExit = categoryOfSuppliesRepository.delete(categoryOfSupplies.getCsupName());
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }else{
            if(operationMode == FXOperationMode.CREATE){
                categoryNameField.requestFocus();
            }else{
                btCancel.requestFocus();
            }
        }
    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

}
