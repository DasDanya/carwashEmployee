package ru.pin120.carwashemployee.Transport;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * FX представление транспорта
 */
public class TransportFX {

    public static final int MAX_LENGTH_MARK=50;
    public static final int MAX_LENGTH_MODEL=50;
    public static final String MARK_REGEX = "^[A-Za-zА-Яа-яЁё\\s-]+$";
    public static final String MODEL_REGEX = "^[A-Za-zА-Яа-яЁё0-9\\s-]+$";

    private LongProperty trId;
    private  StringProperty trMark;
    private StringProperty trModel;
    private  StringProperty trCategory;

    public long getTrId() {
        return trId.get();
    }

    public LongProperty trIdProperty() {
        return trId;
    }

    public String getTrMark() {
        return trMark.get();
    }

    public StringProperty trMarkProperty() {
        return trMark;
    }

    public String getTrModel() {
        return trModel.get();
    }

    public StringProperty trModelProperty() {
        return trModel;
    }

    public String getTrCategory() {
        return trCategory.get();
    }

    public StringProperty trCategoryProperty() {
        return trCategory;
    }

    public TransportFX(Long trId, String trMark, String trModel, String trCategory) {
        this.trId = new SimpleLongProperty(trId);
        this.trMark = new SimpleStringProperty(trMark);
        this.trModel = new SimpleStringProperty(trModel);
        this.trCategory = new SimpleStringProperty(trCategory);
    }
}

