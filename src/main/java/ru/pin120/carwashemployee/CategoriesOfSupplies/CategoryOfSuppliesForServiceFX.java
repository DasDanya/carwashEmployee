package ru.pin120.carwashemployee.CategoriesOfSupplies;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.control.CheckBox;

/**
 * FX представление привязки категории расходных материалов к услуге
 */
public class CategoryOfSuppliesForServiceFX {
    /**
     * Checkbox, указывающий нужна ли привязка
     */
    private ObjectProperty<CheckBox> select;

    /**
     * Название категории
     */
    private StringProperty csupName;

    /**
     * Единица измерения
     */
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

    /**
     * Конструктор для создания объекта CategoryOfSuppliesForServiceFX
     * @param select  Checkbox, указывающий нужна ли привязка
     * @param csupName Название категории
     * @param unit Единица измерения
     */
    public CategoryOfSuppliesForServiceFX(CheckBox select, String csupName, UnitOfMeasure unit) {
        this.select = new SimpleObjectProperty<>(select);
        this.csupName = new SimpleStringProperty(csupName);
        this.unit = new SimpleStringProperty(unit.getDisplayValue());
    }
}
