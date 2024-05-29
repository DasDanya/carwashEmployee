package ru.pin120.carwashemployee.Supplies;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Boxes.BoxesRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.SuppliesInBox.SuppliesInBox;
import ru.pin120.carwashemployee.SuppliesInBox.SuppliesInBoxRepository;

import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddSupplyInBoxController implements Initializable {
    @FXML
    private ComboBox<Box> boxesComboBox;
    @FXML
    private Spinner<Integer> countSpinner;
    @FXML
    private Button btOK;
    @FXML
    private Stage stage;

    @Getter
    private FXFormExitMode exitMode;


    private ResourceBundle rb;
    private BoxesRepository boxesRepository = new BoxesRepository();
    private SuppliesInBoxRepository suppliesInBoxRepository = new SuppliesInBoxRepository();
    private List<Box> boxes;

    private Supply supply;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
        settingBoxesComboBox();
    }

    private void settingCountSpinner(int maxValue) {
        countSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxValue,0,1));
        TextFormatter<Integer> formatter = new TextFormatter<>(change -> {
            if (change.getControlNewText().matches("\\d*")) {
                int newValue = Integer.parseInt(change.getControlNewText());
                if (newValue <= maxValue) {
                    return change;
                }
            }
            return null;
        });

        countSpinner.getEditor().setTextFormatter(formatter);
    }

    public void setParameters(Supply supply, Stage stage) {
        this.supply = supply;
        this.stage = stage;

        settingCountSpinner(supply.getSupCount());

        this.stage.setTitle(rb.getString("FORM_TITLE"));
        this.stage.setMaxHeight(150);
        closeWindowAction();
    }

    private void closeWindowAction() {
        stage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }


    private void settingBoxesComboBox(){
        try{
            boxes = boxesRepository.getAll();
            boxesComboBox.getItems().setAll(boxes);
            boxesComboBox.getSelectionModel().selectFirst();

            if(boxesComboBox.getItems().isEmpty()){
                btOK.setDisable(true);
            }

            boxesComboBox.setConverter(new StringConverter<Box>() {
                @Override
                public String toString(Box box) {
                    if(box == null){
                        return "";
                    }
                    return box.getBoxId().toString();
                }

                @Override
                public Box fromString(String boxIdString) {
                   Long boxId = Long.valueOf(boxIdString);
                   return boxes.stream()
                           .filter(b-> Objects.equals(b.getBoxId(), boxId))
                           .findFirst()
                           .orElse(null);
                }
            });

        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
    }


    public void btOKAction(ActionEvent actionEvent) {
        if(countSpinner.getValue() == 0){
            FXHelper.showErrorAlert(rb.getString("NOT_ZERO_COUNT"));
            countSpinner.requestFocus();
        }else {
            SuppliesInBox suppliesInBox = new SuppliesInBox();
            suppliesInBox.setCountSupplies(countSpinner.getValue());
            suppliesInBox.setSupply(supply);
            suppliesInBox.setBox(boxesComboBox.getValue());

            try {
                SuppliesInBox addedSuppliesInBox = suppliesInBoxRepository.add(suppliesInBox);
                if (addedSuppliesInBox != null) {
                    exitMode = FXFormExitMode.OK;
                    stage.close();
                }
            } catch (Exception e) {
                FXHelper.showErrorAlert(e.getMessage());
            }
        }

    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        stage.close();
    }
}
