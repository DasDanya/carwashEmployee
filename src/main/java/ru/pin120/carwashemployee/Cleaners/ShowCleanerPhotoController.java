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

/**
 * Контроллер отображения фотографии мойщика
 */
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

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
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

    /**
     * Возвращает текущее окно (Stage), к которому привязан элемент photoImageView.
     *
     * @return текущее окно (Stage)
     */
    private Stage getActualStage(){
        return (Stage) photoImageView.getScene().getWindow();
    }

    /**
     * Отображает выбранное изображение в элементе photoImageView и устанавливает заголовок текущего окна.
     *
     * @param selectedPhoto выбранное изображение для отображения
     */
    public void showPhoto(Image selectedPhoto){
        if(selectedPhoto != null){
            photo = selectedPhoto;
        }

        photoImageView.setImage(photo);
        getActualStage().setTitle(rb.getString("TITLE"));
        
    }

    /**
     * Отображает выбранное изображение в элементе photoImageView и устанавливает заголовок текущего окна
     * с информацией о ФИО уборщика.
     * @param selectedPhoto выбранное изображение для отображения
     * @param cleaner объект Cleaner, содержащий информацию о мойщике
     */
    public void showPhoto(Image selectedPhoto, Cleaner cleaner) {
        if(selectedPhoto != null){
            photo = selectedPhoto;
        }

        photoImageView.setImage(photo);
        String fio = cleaner.getClrPatronymic() != null ? String.format(" %s %s %s", cleaner.getClrSurname(), cleaner.getClrName(), cleaner.getClrPatronymic()) : String.format(" %s %s", cleaner.getClrSurname(), cleaner.getClrName());
        getActualStage().setTitle(rb.getString("TITLE") + fio);
    }

    /**
     * Отображает фотографию мойщика в элементе photoImageView
     * и устанавливает заголовок текущего окна с информацией о ФИО уборщика.
     *
     * @param cleaner объект Cleaner, содержащий информацию о мойщике
     */
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

    /**
     * Обработчик события "MouseDragged" для элемента AnchorPane.
     * Перемещает окно (rootAnchorPane) в соответствии с перемещением мыши.
     *
     * @param mouseEvent событие "MouseDragged", вызванное перемещением мыши
     */
    public void onAnchorPaneMouseDragged(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            double newX = mouseEvent.getSceneX() - initialX;
            double newY = mouseEvent.getSceneY() - initialY;
            rootAnchorPane.setLayoutX(newX);
            rootAnchorPane.setLayoutY(newY);
        }
    }

    /**
     * Обработчик события "MousePressed" для элемента AnchorPane.
     * Запоминает начальные координаты мыши для использования при перемещении окна (rootAnchorPane).
     *
     * @param mouseEvent событие "MousePressed", вызванное нажатием кнопки мыши
     */
    public void onAnchorPaneMousePressed(MouseEvent mouseEvent) {
        if (mouseEvent.isPrimaryButtonDown()) {
            initialX = mouseEvent.getSceneX() - rootAnchorPane.getLayoutX();
            initialY = mouseEvent.getSceneY() - rootAnchorPane.getLayoutY();
        }
    }

    /**
     * Обработчик события прокрутки (ScrollEvent) для элемента AnchorPane (rootAnchorPane).
     * Масштабирует окно (rootAnchorPane) в зависимости от направления прокрутки мыши.
     * При прокрутке вверх увеличивает масштаб, при прокрутке вниз уменьшает масштаб (если масштаб > 0.5).
     *
     * @param scrollEvent событие прокрутки (ScrollEvent), вызванное прокруткой колеса мыши
     */
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
