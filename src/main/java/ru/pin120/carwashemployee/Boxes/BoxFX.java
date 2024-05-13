package ru.pin120.carwashemployee.Boxes;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class BoxFX {

    private LongProperty boxId;
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

    public BoxFX(Long boxId, BoxStatus boxStatus) {
        this.boxId = new SimpleLongProperty(boxId);
        this.boxStatus = new SimpleStringProperty(boxStatus.getDisplayValue());
    }
}
