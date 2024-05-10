package ru.pin120.carwashemployee.FX;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.StartApplication;

import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class FXHelper {

    public static void bindHotKeysToDoOperation(Scene scene, Consumer<FXOperationMode> doOperation, Runnable refresh){
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            final KeyCombination keyCombDelete = new KeyCodeCombination(KeyCode.F6, KeyCombination.SHIFT_DOWN);

            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case F4:
                        doOperation.accept(FXOperationMode.EDIT);
                        break;
                    case F5:
                        refresh.run();
                        break;
                    case F6:
                        if(keyCombDelete.match(keyEvent)){
                            doOperation.accept(FXOperationMode.DELETE);
                        }else{
                            doOperation.accept(FXOperationMode.CREATE);
                        }
                        break;
                    default:
                        keyEvent.consume();
                }
            }
        });
    }

    public static void bindHotKeysToDoOperation(Scene scene, Consumer<FXOperationMode> doOperation, Runnable bind, Runnable refresh){
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            final KeyCombination keyCombDelete = new KeyCodeCombination(KeyCode.F6, KeyCombination.SHIFT_DOWN);
            final KeyCombination keyCombBind = new KeyCodeCombination(KeyCode.B, KeyCombination.CONTROL_DOWN);
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case F4:
                        doOperation.accept(FXOperationMode.EDIT);
                        break;
                    case F5:
                        refresh.run();
                        break;
                    case F6:
                        if(keyCombDelete.match(keyEvent)){
                            doOperation.accept(FXOperationMode.DELETE);
                        }else{
                            doOperation.accept(FXOperationMode.CREATE);
                        }
                        break;
                    case B:
                        if(keyCombBind.match(keyEvent)){
                            bind.run();
                        }
                        break;
                    default:
                        keyEvent.consume();
                }
            }
        });
    }
    public static void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(AppHelper.getErrorText());
        alert.setHeaderText(null);
        alert.setWidth(800);

        Window window = alert.getDialogPane().getScene().getWindow();
        window.centerOnScreen();

        alert.showAndWait();
    }

    public static void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(AppHelper.getInfoText());
        alert.initModality(Modality.NONE);
        alert.setHeaderText(null);
        alert.setWidth(800);
        alert.showAndWait();
    }

    public static void setContextMenuForTextField(TextField textField){
        MenuItem cutItem = new MenuItem(AppHelper.getCut());
        MenuItem copyItem = new MenuItem(AppHelper.getCopy());
        MenuItem pasteItem = new MenuItem(AppHelper.getPaste());

        cutItem.setOnAction(event -> textField.cut());
        copyItem.setOnAction(event -> textField.copy());
        pasteItem.setOnAction(event -> textField.paste());

        ContextMenu contextMenu = new ContextMenu(cutItem, copyItem, pasteItem);
        textField.setContextMenu(contextMenu);
    }

    public  static FXWindowData createModalWindow(String pathToBundle, String pathToFXML, Scene actualScene) throws Exception {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle(pathToBundle, locale);
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource(pathToFXML), bundle);
        Parent root = loader.load();

        Scene modalScene = new Scene(root);
        Stage modalStage = new Stage();
        modalStage.setScene(modalScene);
        modalStage.initModality(Modality.WINDOW_MODAL);
        modalStage.initOwner(actualScene.getWindow());
        modalStage.getIcons().add(AppHelper.getMainIcon());

        return new FXWindowData(loader, modalStage);
    }

}
