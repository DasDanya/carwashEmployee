package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

public class CategoryOfSuppliesForServiceFX {
    private ObjectProperty<CheckBox> select;
    private StringProperty csupName;

    private StringProperty unit;

    public CheckBox getSelect() {
        return select.get();
    }

    public ObjectProperty<CheckBox> selectProperty() {
        return select;
    }

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

    public CategoryOfSuppliesForServiceFX(CheckBox select, String csupName, UnitOfMeasure unit) {
        this.select = new SimpleObjectProperty<>(select);
        this.csupName = new SimpleStringProperty(csupName);
        this.unit = new SimpleStringProperty(unit.getDisplayValue());
    }
}
