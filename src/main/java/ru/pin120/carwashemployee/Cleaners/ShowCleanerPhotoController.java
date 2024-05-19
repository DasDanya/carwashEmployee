package ru.pin120.carwashemployee.Cleaners;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ShowCleanerPhotoController implements Initializable {
    private ResourceBundle rb;

    @FXML
    private AnchorPane rootAnchorPane;
    @FXML
    private ImageView photoImageView;
    @Getter
    private Image photo;
    private double initialX;
    private double initialY;
    double scaleXMultiplyer = 1.0;
    double scaleYMultiplyer = 1.0;
    double imageWidth = 600;
    double imageHeight = 600;
    private CleanersRepository cleanersRepository = new CleanersRepository();
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        photo = AppHelper.getDefaultAvatar();
        photoImageView.setFitWidth(imageWidth);
        photoImageView.setFitHeight(imageHeight);

        Platform.runLater(()->{
            getActualStage().setHeight(imageHeight + 45);
            getActualStage().setWidth(imageWidth + 20);
            rootAnchorPane.setStyle("-fx-background-color: white;");

            rootAnchorPane.setScaleX(scaleXMultiplyer);
            rootAnchorPane.setScaleY(scaleYMultiplyer);

            getActualStage().setResizable(false);
            getActualStage().centerOnScreen();
        });
    }

    private Stage getActualStage(){
        return (Stage) photoImageView.getScene().getWindow();
    }

    public void showPhoto(Image selectedPhoto){
        if(selectedPhoto != null){
            photo = selectedPhoto;
        }

        photoImageView.setImage(photo);
        getActualStage().setTitle(rb.getString("TITLE"));
        
    }

    public void showPhoto(Cleaner cleaner){
        try{
            photo = cleanersRepository.getPhoto(cleaner.getClrPhotoName());
            photoImageView.setImage(photo);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }
        String fio = cleaner.getClrPatronymic() != null ? String.format(" %s %s %s", cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic()) : String.format(" %s %s", cleaner.getClrSurname(), cleaner.getClrName());
        getActualStage().setTitle(rb.getString("TITLE") + fio);
    }

    public void onAnchorPaneMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            double newX = mouseEvent.getSceneX() - initialX;
            double newY = mouseEvent.getSceneY() - initialY;
            rootAnchorPane.setLayoutX(newX);
            rootAnchorPane.setLayoutY(newY);
        }
    }

    public void onAnchorPaneMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            initialX = mouseEvent.getSceneX() - rootAnchorPane.getLayoutX();
            initialY = mouseEvent.getSceneY() - rootAnchorPane.getLayoutY();
        }
    }

    public void onScrollingView(ScrollEvent scrollEvent) {
        double deltaY = scrollEvent.getDeltaY();
        if (deltaY > 0) {
            // Прокрутка вверх
            rootAnchorPane.setScaleX(scaleXMultiplyer += 0.05);
            rootAnchorPane.setScaleY(scaleYMultiplyer += 0.05);

        } else if (rootAnchorPane.getScaleX() > 0.5) {
            // Прокрутка вниз
            rootAnchorPane.setScaleX(scaleXMultiplyer -= 0.05);
            rootAnchorPane.setScaleY(scaleYMultiplyer -= 0.05);
        }
    }
}
