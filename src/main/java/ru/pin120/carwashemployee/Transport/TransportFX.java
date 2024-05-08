package ru.pin120.carwashemployee.Transport;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class TransportFX {
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

