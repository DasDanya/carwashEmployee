package ru.pin120.carwashemployee.FX;

import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.StartApplication;

import java.io.File;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

/**
 * Класс FXHelper предоставляет утилитарные методы для приложений на JavaFX, включая обработку горячих клавиш,
 * отображение предупреждений, настройку контекстного меню для текстовых полей, создание модальных и обычных окон,
 * а также конвертацию файлов в изображения.
 */
public class FXHelper {


    /**
     * Связывает горячие клавиши для выполнения операций на основе предоставленной сцены.
     * Позволяет выполнять операции, такие как создание, редактирование, удаление и обновление.
     *
     * @param scene       Сцена
     * @param doOperation Функция-потребитель для выполнения операций в зависимости от режима FXOperationMode.
     * @param refresh     Выполняемая операция для обновления сцены или данных.
     */
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

    /**
     * Связывает горячие клавиши для выполнения операций на основе предоставленной сцены.
     * Позволяет выполнять операции, такие как создание, редактирование, удаление и обновление.
     *
     * @param scene       Сцена
     * @param doOperation Функция-потребитель для выполнения операций в зависимости от режима FXOperationMode.
     * @param bind        Выполняемая операция для привязки.
     * @param refresh     Выполняемая операция для обновления сцены или данных.
     */
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

    /**
     * Связывает горячие клавиши для выполнения операции обновления на основе предоставленной сцены.
     *
     * @param scene   Сцена
     * @param refresh Выполняемая операция для обновления сцены или данных.
     */
    public static void bindHotKeysToDoOperation(Scene scene, Runnable refresh){
        scene.addEventFilter(KeyEvent.KEY_RELEASED, new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode() == KeyCode.F5){
                    refresh.run();
                }
            }
        });
    }

    /**
     * Отображает диалоговое окно с предупреждением об ошибке.
     *
     * @param message Текст сообщения об ошибке.
     */
    public static void showErrorAlert(String message){
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.setTitle(AppHelper.getErrorText());
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefSize(800,150);

        Window window = alert.getDialogPane().getScene().getWindow();
        window.centerOnScreen();

        alert.showAndWait();
    }

    /**
     * Отображает диалоговое окно с информационным сообщением.
     *
     * @param message Текст информационного сообщения.
     */
    public static void showInfoAlert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.setTitle(AppHelper.getInfoText());
        alert.initModality(Modality.NONE);
        alert.setHeaderText(null);
        alert.getDialogPane().setPrefSize(800,150);

        Window window = alert.getDialogPane().getScene().getWindow();
        window.centerOnScreen();

        alert.showAndWait();
    }

    /**
     * Настраивает контекстное меню для редактируемого текстового поля.
     *
     * @param textField Редактируемое текстовое поле, для которого настраивается контекстное меню.
     */
    public static void setContextMenuForEditableTextField(TextField textField){
        MenuItem cutItem = new MenuItem(AppHelper.getCut());
        MenuItem copyItem = new MenuItem(AppHelper.getCopy());
        MenuItem pasteItem = new MenuItem(AppHelper.getPaste());

        cutItem.setOnAction(event -> textField.cut());
        copyItem.setOnAction(event -> textField.copy());
        pasteItem.setOnAction(event -> textField.paste());

        ContextMenu contextMenu = new ContextMenu(cutItem, copyItem, pasteItem);
        textField.setContextMenu(contextMenu);
    }

    /**
     * Настраивает контекстное меню для нередактируемого текстового поля.
     *
     * @param textField Нередактируемое текстовое поле, для которого настраивается контекстное меню.
     */
    public static void setContextMenuForNotEditableTextField(TextField textField){
        MenuItem copyItem = new MenuItem(AppHelper.getCopy());
        copyItem.setOnAction(event -> textField.copy());

        ContextMenu contextMenu = new ContextMenu(copyItem);
        textField.setContextMenu(contextMenu);
    }

    /**
     * Создает модальное окно на основе указанного пути к ресурсам и FXML-файлу.
     *
     * @param pathToBundle Путь к файлу ресурсов.
     * @param pathToFXML   Путь к FXML-файлу.
     * @param actualScene  Текущая сцена, к которой привязывается модальное окно.
     * @return Объект FXWindowData, содержащий загрузчик FXML и модальное окно.
     * @throws Exception Если возникает ошибка при создании окна.
     */
    public static FXWindowData createModalWindow(String pathToBundle, String pathToFXML, Scene actualScene) throws Exception {
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
        modalStage.centerOnScreen();

        return new FXWindowData(loader, modalStage);
    }

    /**
     * Создает обычное окно на основе указанного пути к ресурсам и FXML-файлу.
     *
     * @param pathToBundle Путь к файлу ресурсов.
     * @param pathToFXML   Путь к FXML-файлу.
     * @return Объект FXWindowData, содержащий загрузчик FXML и созданное окно.
     * @throws Exception Если возникает ошибка при создании окна.
     */
    public static FXWindowData createWindow(String pathToBundle, String pathToFXML) throws Exception {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle(pathToBundle, locale);
        FXMLLoader loader = new FXMLLoader(StartApplication.class.getResource(pathToFXML), bundle);
        Parent root = loader.load();

        Scene modalScene = new Scene(root);
        Stage modalStage = new Stage();
        modalStage.setScene(modalScene);
        modalStage.getIcons().add(AppHelper.getMainIcon());
        modalStage.centerOnScreen();

        return new FXWindowData(loader, modalStage);
    }


    /**
     * Конвертирует файл в изображение типа Image.
     *
     * @param photoAsFile Файл, который необходимо конвертировать в изображение.
     * @return Объект Image, представляющий сконвертированное изображение.
     */
    public static Image convertToImage(File photoAsFile){
        if(photoAsFile != null) {
            return new Image(photoAsFile.toURI().toString());
        }
        return null;
    }

    /**
     * Отображает диалоговое окно подтверждения с указанным заголовком и текстом.
     *
     * @param title       Заголовок диалогового окна.
     * @param contextText Текст, отображаемый в диалоговом окне.
     * @return Optional, содержащий результат диалога
     */
    public static Optional<ButtonType> createConfirmAlert(String title, String contextText){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", ButtonType.OK);
        alert.getDialogPane().setPrefSize(800,50);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(contextText);

        return alert.showAndWait();
    }


}
