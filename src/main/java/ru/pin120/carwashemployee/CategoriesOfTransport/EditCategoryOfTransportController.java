package ru.pin120.carwashemployee.CategoriesOfTransport;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * ���������� �������������� ������ � ��������� ����������
 */
public class EditCategoryOfTransportController implements Initializable {

    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private TextField categoryNameField;
    private ResourceBundle rb;
    @FXML
    private Stage stage;

    private FXOperationMode operationMode;

    private CategoryOfTransport categoryOfTransport;

    @Getter
    private FXFormExitMode exitMode;

    private CategoryOfTransportRepository categoryOfTransportRepository = new CategoryOfTransportRepository();

    /**
     * ������������� �����������
     *
     * @param url URL ������������ FXML �����
     * @param resourceBundle ����� �������� ��� �����������
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        FXHelper.setContextMenuForEditableTextField(categoryNameField);
        categoryNameFieldTextListener();
    }

    /**
     * ������������� ��������� ��������� ������ ��� ���� �������� ���������.
     * ��������� ����� ���������� �������� � �������� ��� �� ����������� ���������� �����, ���� ����������.
     */
    private void categoryNameFieldTextListener() {
        categoryNameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                if (newValue.length() > CategoryOfTransportFX.MAX_LENGTH_CATEGORY_NAME) {
                    categoryNameField.setText(oldValue);
                }
            }
        });
    }

    /**
     * ������������� ��������� ��� ����� ��������, ���������, �������� ��������� ����������.
     *
     * @param categoryOfTransport ������ {@link CategoryOfTransport}, ���������� ������ � ��������� ����������.
     * @param operationMode ����� �������� (��������, ���������, ��������).
     * @param stage ��������� ����, �� ������� ������������ �����.
     */
    public void setParameters(CategoryOfTransport categoryOfTransport, FXOperationMode operationMode, Stage stage){
        this.categoryOfTransport = categoryOfTransport;
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
                categoryNameField.setEditable(false);
                Platform.runLater(()->btOK.requestFocus());
                break;

        }
        categoryNameField.setText(categoryOfTransport.getCatTrName());
        this.stage.setMaxHeight(130);
        closeWindowAction();
    }

    /**
     * ������������� �������� �� ������� �������� ����.
     * ������������� ����� ���������� ����� �� "�����" ��� �������� ���� �������������.
     */
    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

    /**
     * ���������� ������� ������� ������ "OK".
     *
     * @param actionEvent ������� ��������, �������������� �������� ������ "OK".
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(categoryNameField.getText() == null || categoryNameField.getText().isBlank()) {
            FXHelper.showErrorAlert(rb.getString("EMPTY_CATEGORY_NAME"));
            categoryNameField.clear();
            categoryNameField.requestFocus();
        }else if(!categoryNameField.getText().matches(CategoryOfTransportFX.REGEX)){
            FXHelper.showErrorAlert(rb.getString("CATEGORY_NAME_VALID_CHARACTERS"));
            categoryNameField.requestFocus();
        } else{
            try{
               switch (operationMode){
                   case CREATE:
                       categoryOfTransport.setCatTrName(categoryNameField.getText().trim());
                       CategoryOfTransport createdCategoryOfTransport = categoryOfTransportRepository.createCategoryOfTransport(categoryOfTransport);
                       if(createdCategoryOfTransport != null) {
                           canExit = true;
                           categoryOfTransport.setCatTrId(createdCategoryOfTransport.getCatTrId());
                           categoryOfTransport.setCatTrName(createdCategoryOfTransport.getCatTrName());
                       }
                       break;
                   case EDIT:
                       if(categoryOfTransport.getCatTrName().equals(categoryNameField.getText().trim())){
                           canExit = true;
                       }else{
                           categoryOfTransport.setCatTrName(categoryNameField.getText().trim());
                           CategoryOfTransport editedCategoryOfTransport = categoryOfTransportRepository.editCategoryOfTransport(categoryOfTransport);
                           if(editedCategoryOfTransport != null){
                               canExit = true;
                               categoryOfTransport.setCatTrId(editedCategoryOfTransport.getCatTrId());
                               categoryOfTransport.setCatTrName(editedCategoryOfTransport.getCatTrName());
                           }
                       }
                       break;
                   case DELETE:
                       canExit = categoryOfTransportRepository.deleteCategoryOfTransport(categoryOfTransport.getCatTrId());
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

    /**
     * ���������� ������� ������� ������ "������".
     * ������������� ����� ���������� ����� �� CANCEL � ��������� ��������� ����.
     *
     * @param actionEvent ������� ��������, �������������� �������� ������ "������".
     */
    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }
}
