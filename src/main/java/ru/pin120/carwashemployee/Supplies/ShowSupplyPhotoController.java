package ru.pin120.carwashemployee.Supplies;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lombok.Getter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.Cleaners.CleanersRepository;
import ru.pin120.carwashemployee.FX.FXHelper;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Контроллер для отображения фотографии расходного материала
 */
public class ShowSupplyPhotoController implements Initializable {
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
    private SupplyRepository supplyRepository = new SupplyRepository();

    /**
     * Инициализация контроллера
     *
     * @param url URL расположения FXML файла
     * @param resourceBundle Набор ресурсов для локализации
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        rb = resourceBundle;

        photo = AppHelper.getNoPhoto();
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
     * с информацией о расходном материале
     * @param selectedPhoto выбранное изображение для отображения
     * @param supply объект Supply, содержащий информацию о расходном материале
     */
    public void showPhoto(Image selectedPhoto, Supply supply) {
        if(selectedPhoto != null){
            photo = selectedPhoto;
        }

        photoImageView.setImage(photo);
        getActualStage().setTitle(String.format("%s %s %s %d %s", rb.getString("TITLE"), supply.getCategory().getCsupName(), supply.getSupName(), supply.getSupMeasure(), supply.getCategory().getUnit().getDisplayValue()));
    }

    /**
     * Отображает фотографию расходного материала в элементе photoImageView
     * и устанавливает заголовок текущего окна с информацией о расходном материале
     *
     * @param supply объект Supply, содержащий информацию о расходном материале
     */
    public void showPhoto(Supply supply){
        try{
            photo = supplyRepository.getPhoto(supply.getSupPhotoName());
            photoImageView.setImage(photo);
        }catch (Exception e){
            FXHelper.showErrorAlert(e.getMessage());
        }

        getActualStage().setTitle(String.format("%s %s %s %d %s", rb.getString("TITLE"), supply.getCategory().getCsupName(), supply.getSupName(), supply.getSupMeasure(), supply.getCategory().getUnit().getDisplayValue()));
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
