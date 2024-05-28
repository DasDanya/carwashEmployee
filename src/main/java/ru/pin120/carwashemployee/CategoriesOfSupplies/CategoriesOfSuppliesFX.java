package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CategoriesOfSuppliesFX {

    private StringProperty csupName;
    private StringProperty unit;

    public String getCsupName() {
        return csupName.get();
    }

    public StringProperty csupNameProperty() {
        return csupName;
    }

    public String getUnit() {
        return unit.get();
    }

    public StringProperty unitProperty() {
        return unit;
    }

    public CategoriesOfSuppliesFX(String csupName, UnitOfMeasure unit) {
        this.csupName = new SimpleStringProperty(csupName);
        this.unit = new SimpleStringProperty(unit.getDisplayValue());
    }
}
