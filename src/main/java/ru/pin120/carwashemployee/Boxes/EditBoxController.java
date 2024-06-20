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
 * Контроллер редактирования данных о боксе
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
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }


    /**
     * Устанавливает параметры для контроллера редактирования бокса.
     * В зависимости от режима (создание, редактирование или удаление),
     * устанавливает заголовок окна, заполняет комбо-бокс статуса и устанавливает
     * соответствующие значения выбранных элементов комбо-бокса.
     *
     * @param box           Бокс, с которым проводится операция.
     * @param operationMode Режим операции (CREATE, EDIT или DELETE).
     * @param stage         Stage, для которой устанавливаются параметры.
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
     * Заполняет комбо-бокс статуса значениями из перечисления BoxStatus.
     * Конвертер устанавливает отображаемое значение для каждого элемента комбо-бокса.
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
     * Обработчик события нажатия кнопки OK в окне редактирования ящика.
     * @param actionEvent Событие нажатия кнопки OK.
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
     * Обработчик события нажатия кнопки Отмена в окне редактирования ящика.
     * Устанавливает режим выхода из формы как CANCEL и закрывает окно.
     *
     * @param actionEvent Событие нажатия кнопки Отмена.
     */
    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }

    /**
     * Если окно закрывается, устанавливает режим выхода из формы как EXIT.
     */
    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }
}
