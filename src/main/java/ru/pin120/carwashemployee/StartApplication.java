package ru.pin120.carwashemployee;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Главный класс приложения, инициализирующий и запускающий JavaFX приложение.
 */
public class StartApplication extends Application {

    /**
     * Метод start, вызываемый при запуске приложения JavaFX.
     *
     * @param stage Главное окно приложения, которое будет отображаться.
     * @throws IOException Если возникают проблемы с загрузкой ресурсов или FXML-файла.
     */
    @Override
    public void start(Stage stage) throws IOException {
        AppHelper.loadProperties();
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.Users.resources.UserAuthorization", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("Users/fxml/UserAuthorization.fxml"),bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(bundle.getString("FORM_TITLE"));
        stage.setScene(scene);
        stage.setMaxHeight(170);

        stage.getIcons().add(AppHelper.getMainIcon());
        stage.show();
    }

    /**
     * Метод main, точка входа в приложение.
     *
     * @param args Аргументы командной строки, если они указаны.
     */
    public static void main(String[] args) {
        launch();
    }
}