package ru.pin120.carwashemployee.CategoriesOfServices;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.CategoriesAndServices.CategoriesOfServicesRepository;
import ru.pin120.carwashemployee.CategoriesAndServices.ServiceRepository;
import ru.pin120.carwashemployee.FX.FXFormExitMode;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BindWithCategoryController implements Initializable {

    @FXML
    private Button btOK;
    @FXML
    private Button btCancel;
    @FXML
    private ComboBox<String> categoriesCombobox;
    @FXML
    private Stage parentStage;

    //private BindWithCategoryMode bindMode;
    private String parameter;

    @Getter
    private FXFormExitMode exitMode;

    private ResourceBundle rb;

    private ServiceRepository serviceRepository = new ServiceRepository();
    private CategoriesOfServicesRepository categoriesOfServicesRepository = new CategoriesOfServicesRepository();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;
    }

//    public void setParameters(Stage stage, BindWithCategoryMode bindMode, String currentCategory,String parameter){
//        parentStage = stage;
//        this.bindMode = bindMode;
//        this.parameter = parameter;
//
//        try {
//            List<String> categories = categoriesOfServicesRepository.getCategoriesName();
//            categories.remove(currentCategory);
//            if(categories.isEmpty()){
//                btOK.setDisable(true);
//            }else{
//                categoriesCombobox.getItems().setAll(categories);
//                categoriesCombobox.getSelectionModel().selectFirst();
//            }
//        } catch (Exception e) {
//            FXHelper.showErrorAlert(e.getMessage());
//        }
//
//        if(bindMode == BindWithCategoryMode.CATEGORY){
//            parentStage.setTitle(parameter + rb.getString("CATEGORY_BIND_TITLE"));
//        }else{
//            parentStage.setTitle(parameter + rb.getString("SERVICE_BIND_TITLE"));
//        }
//
//        parentStage.setMaxHeight(130);
//        closeWindowAction();
//    }
//
//    public void btOKAction(ActionEvent actionEvent) {
//        boolean canExit = false;
//        try {
//            if (bindMode == BindWithCategoryMode.CATEGORY) {
//                canExit = serviceRepository.bindServicesToCategory(parameter, categoriesCombobox.getValue());
//            } else {
//                canExit = serviceRepository.bindServiceToCategory(parameter, categoriesCombobox.getValue());
//            }
//        }catch (Exception e){
//            FXHelper.showErrorAlert(e.getMessage());
//        }
//        if(canExit){
//            exitMode = FXFormExitMode.OK;
//            parentStage.close();
//        }else{
//            btCancel.requestFocus();
//        }
//    }

    public void btCancelAction(ActionEvent actionEvent) {
        exitMode = FXFormExitMode.CANCEL;
        parentStage.close();
    }

    private void closeWindowAction(){
        parentStage.setOnCloseRequest(event -> exitMode = FXFormExitMode.EXIT);
    }

}
