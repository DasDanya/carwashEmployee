package ru.pin120.carwashemployee.CategoriesOfCars;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

public class EditCategoryOfCarsController implements Initializable {
    private static final int MAX_LENGTH_CATEGORY_NAME = 50;
    @FXML
    private Button btCancel;
    @FXML
    private TextField categoryNameField;
    private ResourceBundle rb;
    @FXML
    private Stage stage;

    private FXOperationMode operationMode;

    private CategoryOfCars categoryOfCars;

    @Getter
    private FXFormExitMode exitMode;

    private CategoryOfCarsRepository categoryOfCarsRepository = new CategoryOfCarsRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForTextField(categoryNameField);
        categoryNameFieldTextListener();
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

    public void setParameters(CategoryOfCars categoryOfCars, FXOperationMode operationMode, Stage stage){
        this.categoryOfCars = categoryOfCars;
        this.operationMode = operationMode;
        this.stage = stage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_FORM_TITLE"));
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_FORM_TITLE"));
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_FORM_TITLE"));
                categoryNameField.setDisable(true);
                break;

        }
        categoryNameField.setText(categoryOfCars.getCatCarsName());
        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(categoryNameField.getText() == null || categoryNameField.getText().isBlank()) {
            FXHelper.showErrorAlert(rb.getString("EMPTY_CATEGORY_NAME"));
            categoryNameField.clear();
            categoryNameField.requestFocus();
        }else{
            try{
               switch (operationMode){
                   case CREATE:
                       categoryOfCars.setCatCarsName(categoryNameField.getText().trim());
                       canExit = categoryOfCarsRepository.createCategoryOfCars(categoryOfCars);
                       break;
                   case DELETE:
                       canExit = categoryOfCarsRepository.deleteCategoryOfCars(categoryNameField.getText().trim());
                       break;
               }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
        }else{
            if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT){
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
}
