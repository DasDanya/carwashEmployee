package ru.pin120.carwashemployee.FX;

import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Класс для открытия нового окна
 */
@AllArgsConstructor
@Getter
public class FXWindowData {
    /**
     * Загрузчик FXML
     */
    private FXMLLoader loader;
    /**
     * Созданное окно
     */
    private Stage modalStage;
}
