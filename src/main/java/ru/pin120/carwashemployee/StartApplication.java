package ru.pin120.carwashemployee;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class StartApplication extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Locale locale = Locale.getDefault();
        ResourceBundle bundle = ResourceBundle.getBundle("ru.pin120.carwashemployee.CategoriesOfCars.resources.CategoriesOfCars", locale);
        FXMLLoader fxmlLoader = new FXMLLoader(StartApplication.class.getResource("CategoriesOfCars/fxml/CategoriesOfCars.fxml"),bundle);
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle(bundle.getString("FORM_TITLE"));
        stage.setScene(scene);

        stage.getIcons().add(AppHelper.getMainIcon());
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}