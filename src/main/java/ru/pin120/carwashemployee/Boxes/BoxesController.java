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
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransportFX;
import ru.pin120.carwashemployee.CategoriesOfTransport.EditCategoryOfTransportController;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.FX.FXOperationMode;
import ru.pin120.carwashemployee.FX.FXWindowData;

import java.net.URL;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class BoxesController implements Initializable {

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
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        numberColumn.setCellValueFactory(c -> c.getValue().boxIdProperty().asObject());
        statusColumn.setCellValueFactory(c -> c.getValue().boxStatusProperty());
        fillingAll();

        settingTooltipForButtons();
        Platform.runLater(() -> FXHelper.bindHotKeysToDoOperation(getActualScene(), this::doOperation, this::doRefresh));
    }

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

    private void fillingObservableList(List<Box> boxes){
        for(Box box: boxes){
            BoxFX boxFX = new BoxFX(box.getBoxId(), box.getBoxStatus());
            boxFXES.add(boxFX);
        }
    }

    private Scene getActualScene(){
        return boxesTable.getScene();
    }

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
                FXWindowData fxWindowData = FXHelper.createModalWindow("ru.pin120.carwashemployee.Boxes.resources.EditBox", "Boxes/fxml/EditBox.fxml", getActualScene());
                EditBoxController editBoxController = fxWindowData.getLoader().getController();

                editBoxController.setParameters(box, operationMode, fxWindowData.getModalStage());
                fxWindowData.getModalStage().showAndWait();
                doResult(operationMode, editBoxController.getExitMode(), box, selectedBoxFX);
            }catch (Exception e){
                FXHelper.showErrorAlert(e.getMessage());
                boxesTable.requestFocus();
            }
        }
    }

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

    private void doRefresh(){}
}
