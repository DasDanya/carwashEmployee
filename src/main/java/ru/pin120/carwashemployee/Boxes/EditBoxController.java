package ru.pin120.carwashemployee.Boxes;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * ���������� �������������� ������ � �����
 */
public class EditBoxController implements Initializable {

    @FXML
    private Button btCancel;
    @FXML
    private ComboBox<BoxStatus> statusComboBox;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    @Getter
    private FXFormExitMode exitMode;
    private FXOperationMode operationMode;
    private Box box;
    private BoxesRepository boxesRepository = new BoxesRepository();

    /**
     * ������������� �����������
     *
     * @param url URL ������������ FXML �����
     * @param resourceBundle ����� �������� ��� �����������
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }


    /**
     * ������������� ��������� ��� ����������� �������������� �����.
     * � ����������� �� ������ (��������, �������������� ��� ��������),
     * ������������� ��������� ����, ��������� �����-���� ������� � �������������
     * ��������������� �������� ��������� ��������� �����-�����.
     *
     * @param box           ����, � ������� ���������� ��������.
     * @param operationMode ����� �������� (CREATE, EDIT ��� DELETE).
     * @param stage         Stage, ��� ������� ��������������� ���������.
     */
    public void setParameters(Box box, FXOperationMode operationMode, Stage stage){
        this.box = box;
        this.operationMode = operationMode;
        this.stage = stage;

        switch (operationMode){
            case CREATE:
                this.stage.setTitle(rb.getString("CREATE_TITLE"));
                fillingStatusComboBox();
                statusComboBox.getSelectionModel().selectFirst();
                break;
            case EDIT:
                this.stage.setTitle(rb.getString("EDIT_TITLE") + " " + box.getBoxId());
                fillingStatusComboBox();
                statusComboBox.getSelectionModel().select(box.getBoxStatus());
                break;
            case DELETE:
                this.stage.setTitle(rb.getString("DELETE_TITLE")+ " " + box.getBoxId());
                fillingStatusComboBox();
                statusComboBox.getSelectionModel().select(box.getBoxStatus());
                statusComboBox.setDisable(true);
                break;
        }

        this.stage.setMaxHeight(140);
        closeWindowAction();
    }

    /**
     * ��������� �����-���� ������� ���������� �� ������������ BoxStatus.
     * ��������� ������������� ������������ �������� ��� ������� �������� �����-�����.
     */
    private void fillingStatusComboBox(){
        statusComboBox.getItems().setAll(BoxStatus.values());
        statusComboBox.setConverter(new StringConverter<BoxStatus>() {
            @Override
            public String toString(BoxStatus status) {
                return status == null ? null : status.getDisplayValue();
            }
            @Override
            public BoxStatus fromString(String string) {
                return null;
            }
        });
    }


    /**
     * ���������� ������� ������� ������ OK � ���� �������������� �����.
     * @param actionEvent ������� ������� ������ OK.
     */
    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        try{
            switch (operationMode){
                case CREATE:
                    box.setBoxStatus(statusComboBox.getSelectionModel().getSelectedItem());
                    Box createdBox = boxesRepository.create(box);
                    if(createdBox != null){
                        box.setBoxId(createdBox.getBoxId());
                        canExit = true;
                    }
                    break;
                case EDIT:
                    BoxStatus selectedStatus = statusComboBox.getSelectionModel().getSelectedItem();
                    if(box.getBoxStatus() == selectedStatus){
                        canExit = true;
                    }else {
                        box.setBoxStatus(selectedStatus);
                        Box editedBox = boxesRepository.edit(box);
                        if(editedBox != null){
                            canExit = true;
                        }
                    }
                    break;
                case DELETE:
                    canExit = boxesRepository.delete(box.getBoxId());
                    break;
            }
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
        if(canExit) {
            exitMode = FXFormExitMode.OK;
            stage.close();
        }else{
            if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.EDIT){
                statusComboBox.requestFocus();
            }else{
                btCancel.requestFocus();
            }
        }
    }

    /**
     * ���������� ������� ������� ������ ������ � ���� �������������� �����.
     * ������������� ����� ������ �� ����� ��� CANCEL � ��������� ����.
     *
     * @param actionEvent ������� ������� ������ ������.
     */
    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    /**
     * ���� ���� �����������, ������������� ����� ������ �� ����� ��� EXIT.
     */
    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
