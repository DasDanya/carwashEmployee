package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * FX представление категории расходных материалов
 */
public class CategoriesOfSuppliesFX {

    /**
     * Название категории
     */
    private StringProperty csupName;
    /**
     * Единица измерения
     */
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

    /**
     * Конструктор для создания объекта CategoriesOfSuppliesFX
     * @param csupName Название категории
     * @param unit Единица измерения
     */
    public CategoriesOfSuppliesFX(String csupName, UnitOfMeasure unit) {
        this.csupName = new SimpleStringProperty(csupName);
        this.unit = new SimpleStringProperty(unit.getDisplayValue());
    }
}
