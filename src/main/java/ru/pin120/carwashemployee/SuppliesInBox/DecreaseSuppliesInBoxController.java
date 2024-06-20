package ru.pin120.carwashemployee.SuppliesInBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.net.URL;
import java.sql.SQLOutput;
import java.util.ResourceBundle;

/**
 * Контроллер уменьшения расходного материала в боксе
 */
public class DecreaseSuppliesInBoxController implements Initializable {

    public Spinner<Integer> countSpinner;
    @FXML
    private Stage stage;
    private ResourceBundle rb;
    @Getter
    private FXFormExitMode exitMode;
    private DecreaseSuppliesInBoxStatus decreaseStatus;
    private SuppliesInBox suppliesInBox;
    private SuppliesInBoxRepository suppliesInBoxRepository = new SuppliesInBoxRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }

    public void setParameters(SuppliesInBox suppliesInBox, DecreaseSuppliesInBoxStatus decreaseSuppliesInBoxStatus, Stage modalStage) {
        this.suppliesInBox = suppliesInBox;
        decreaseStatus = decreaseSuppliesInBoxStatus;
        stage = modalStage;

        if(decreaseStatus == DecreaseSuppliesInBoxStatus.TRANSFER){
            stage.setTitle(rb.getString("FORM_TITLE_TRANSFER"));
        }else{
            stage.setTitle(rb.getString("FORM_TITLE_DECREASE"));
        }

        settingCountSpinner();
        this.stage.setMaxHeight(140);
        closeWindowAction();
    }

    private void settingCountSpinner(){
        countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, suppliesInBox.getCountSupplies(),suppliesInBox.getCountSupplies(),1));
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= suppliesInBox.getCountSupplies()) {
                    return change;
                }
            }
            return null;
        });

        countSpinner.getEditor().setTextFormatter(formatter);
    }

    public void btOKAction(ActionEvent actionEvent) {
        boolean canExit = false;
        if(suppliesInBox.getCountSupplies() == 0){
            canExit = true;
        }else {
            try {
                if (decreaseStatus == DecreaseSuppliesInBoxStatus.TRANSFER) {
                    int countOfAdded = suppliesInBox.getCountSupplies() - countSpinner.getValue();
                    suppliesInBox.setCountSupplies(countSpinner.getValue());
                    AddSuppliesFromBoxDTO addSuppliesFromBoxDTO = new AddSuppliesFromBoxDTO(suppliesInBox, countOfAdded);
                    SuppliesInBox transferedSuppliesInBox = suppliesInBoxRepository.transferToWarehouse(addSuppliesFromBoxDTO);
                    if (transferedSuppliesInBox != null) {
                        canExit = true;
                    }
                } else {
                    suppliesInBox.setCountSupplies(countSpinner.getValue());
                    SuppliesInBox editedSuppliesInBox = suppliesInBoxRepository.edit(suppliesInBox);
                    if(editedSuppliesInBox != null){
                        canExit = true;
                    }
                }
            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
            }
        }
        if(canExit){
            exitMode = FXFormExitMode.OK;
            stage.close();
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
