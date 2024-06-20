package ru.pin120.carwashemployee.Boxes;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * FX представление бокса
 */
public class BoxFX {

    /**
     * id бокса
     */
    private LongProperty boxId;
    /**
     * Статус бокса
     */
    private StringProperty boxStatus;

    public long getBoxId() {
        return boxId.get();
    }

    public LongProperty boxIdProperty() {
        return boxId;
    }

    public String getBoxStatus() {
        return boxStatus.get();
    }

    public StringProperty boxStatusProperty() {
        return boxStatus;
    }

    public void setBoxStatus(String boxStatus) {
        this.boxStatus.set(boxStatus);
    }


    /**
     * Конструктор для создания объекта BoxFX.
     *
     * @param boxId Идентификатор бокса.
     * @param boxStatus Статус бокса в виде строки, представляющей его отображаемое значение.
     */
    public BoxFX(Long boxId, BoxStatus boxStatus) {
        this.boxId = new SimpleLongProperty(boxId);
        this.boxStatus = new SimpleStringProperty(boxStatus.getDisplayValue());
    }
}
