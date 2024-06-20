package ru.pin120.carwashemployee.Boxes;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;
import ru.pin120.carwashemployee.SuppliesInBox.SuppliesInBoxController;
import ru.pin120.carwashemployee.Users.UserRole;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Контроллер формы с боксами
 */
public class BoxesController implements Initializable {

    @FXML
    private Button refreshButton;
    @FXML
    private Button showSuppliesButton;
    @FXML
    private Button createButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private TableView<BoxFX> boxesTable;
    @FXML
    private TableColumn<BoxFX, Long> numberColumn;
    @FXML
    private TableColumn<BoxFX, String> statusColumn;
    private ResourceBundle rb;

    private BoxesRepository boxesRepository = new BoxesRepository();
    private ObservableList<BoxFX> boxFXES = FXCollections.observableArrayList();

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        numberColumn.setCellValueFactory(c -> c.getValue().boxIdProperty().asObject());
        statusColumn.setCellValueFactory(c -> c.getValue().boxStatusProperty());
        boxesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        fillingAll();

        settingTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

    /**
     * Заполняет таблицу информацией о боксах
     */
    private void fillingAll() {
        try{
            List<Box> boxes = boxesRepository.getAll();
            fillingObservableList(boxes);
            boxesTable.setItems(boxFXES);
            boxesTable.getSelectionModel().selectFirst();
            Platform.runLater(()-> boxesTable.requestFocus());
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }

    /**
     * Заполняет ObservableList объектами типа BoxFX на основе списка объектов Box.
     *
     * @param boxes Список объектов Box для заполнения ObservableList.
     */
    private void fillingObservableList(List<Box> boxes){
        for(Box box: boxes){
            BoxFX boxFX = new BoxFX(box.getBoxId(), box.getBoxStatus());
            boxFXES.add(boxFX);
        }
    }

    /**
     * Возвращает текущую сцену, на которой находится таблица боксов
     *
     * @return Текущая сцена, на которой находится таблица боксов
     */
    private Scene getActualScene(){
        return boxesTable.getScene();
    }

    /**
     * Устанавливает всплывающие подсказки для кнопок
     */
    private void settingTooltipForButtons() {
        createButton.setOnMouseEntered(event -> {
            createButton.setTooltip(new Tooltip(rb.getString("CREATE_BOX")));
        });
        deleteButton.setOnMouseEntered(event -> {
            deleteButton.setTooltip(new Tooltip(rb.getString("DELETE_BOX")));
        });
        editButton.setOnMouseEntered(event -> {
            editButton.setTooltip(new Tooltip(rb.getString("EDIT_BOX")));
        });
        refreshButton.setOnMouseEntered(event -> {
            refreshButton.setTooltip(new Tooltip(rb.getString("REFRESH")));
        });
        showSuppliesButton.setOnMouseEntered(event -> {
            showSuppliesButton.setTooltip(new Tooltip(rb.getString("SHOW_SUPPLIES")));
        });
    }

    public void createButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.CREATE);
    }

    public void editButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.EDIT);
    }

    public void deleteButtonAction(ActionEvent actionEvent) {
        doOperation(FXOperationMode.DELETE);
    }

    /**
     * Выполняет операцию над боксом в зависимости от заданного режима операции
     *
     * @param operationMode Режим операции (CREATE, EDIT, DELETE).
     */
    private void doOperation(FXOperationMode operationMode){
        Box box = null;
        BoxFX selectedBoxFX = null;
        switch (operationMode){
            case CREATE:
                box = new Box();
                break;
            case EDIT:
            case DELETE:
                if(boxesTable.getSelectionModel().getSelectedItem() != null){
                    selectedBoxFX = boxesTable.getSelectionModel().getSelectedItem();
                    box = new Box();
                    box.setBoxId(selectedBoxFX.getBoxId());
                    box.setBoxStatus(BoxStatus.valueOfDisplayValue(selectedBoxFX.getBoxStatus()));
                }
                break;
        }
        if(box == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOX"));
            boxesTable.requestFocus();
        }else{
            try{
                boolean canOpenWindow = true;
                if(operationMode == FXOperationMode.CREATE || operationMode == FXOperationMode.DELETE) {
                    if(!AppHelper.getUserInfo().get(2).equals(UserRole.OWNER.name())){
                        canOpenWindow = false;
                        FXHelper.showErrorAlert(AppHelper.getCannotAccessOperationText());
                    }
                }
                if(canOpenWindow) {
                    FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Boxes.resources.EditBox", "Boxes/fxml/EditBox.fxml", getActualScene());
                    EditBoxController editBoxController = fxWindowData.getLoader().getController();

                    editBoxController.setParameters(box, operationMode, fxWindowData.getModalStage());
                    fxWindowData.getModalStage().showAndWait();
                    doResult(operationMode, editBoxController.getExitMode(), box, selectedBoxFX);
                }else{
                    boxesTable.requestFocus();
                }
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                boxesTable.requestFocus();
            }
        }
    }

    /**
     * Обрабатывает результат операции над боксом (CREATE, EDIT, DELETE) в зависимости от режима выхода из формы.
     *
     * @param operationMode Режим операции (CREATE, EDIT, DELETE).
     * @param exitMode      Режим выхода из формы (OK, CANCEL).
     * @param box           Объект бокса, над которым выполнялась операция.
     * @param selectedBoxFX Выбранный объект бокса для редактирования или удаления.
     */
    private void doResult(FXOperationMode operationMode, FXFormExitMode exitMode, Box box, BoxFX selectedBoxFX) {
        if(exitMode == FXFormExitMode.OK){
            switch (operationMode){
                case CREATE:
                    BoxFX boxFX = new BoxFX(box.getBoxId(), box.getBoxStatus());
                    boxFXES.add(boxFX);
                    boxFXES.sort(Comparator.comparing(BoxFX::getBoxId));

                    boxesTable.getSelectionModel().select(boxFX);
                    numberColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case EDIT:
                    selectedBoxFX.setBoxStatus(box.getBoxStatus().getDisplayValue());

                    boxFXES.sort(Comparator.comparing(BoxFX::getBoxId));
                    boxesTable.getSelectionModel().select(selectedBoxFX);
                    numberColumn.setSortType(TableColumn.SortType.ASCENDING);
                    break;
                case DELETE:
                    boxFXES.remove(selectedBoxFX);
                    break;
            }
        }

        boxesTable.requestFocus();
    }


    public void refreshAction(ActionEvent actionEvent) {
        doRefresh();
    }

    /**
     * Очищает ObservableList boxFXES и затем вызывает метод fillingAll() для заполнения таблицы
     * актуальными данными о боксах.
     */
    private void doRefresh(){
        boxFXES.clear();
        fillingAll();
    }

    public void showSuppliesButtonAction(ActionEvent actionEvent) {
        BoxFX boxFX = boxesTable.getSelectionModel().getSelectedItem();
        if(boxFX == null){
            FXHelper.showErrorAlert(rb.getString("NOT_SELECTED_BOX"));
        }else{
            try{
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.SuppliesInBox.resources.SuppliesInBox", "SuppliesInBox/fxml/SuppliesInBox.fxml", getActualScene());
                SuppliesInBoxController suppliesInBoxController = fxWindowData.getLoader().getController();
                suppliesInBoxController.startFilling(boxFX.getBoxId(), fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                boxesTable.requestFocus();
            }
        }

        boxesTable.requestFocus();
    }
}
