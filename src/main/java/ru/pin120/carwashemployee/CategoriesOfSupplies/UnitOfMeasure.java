package ru.pin120.carwashemployee.CategoriesOfSupplies;

import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.BoxStatus;

public enum UnitOfMeasure {

    MILLILITERS("мл"),
    PIECE("шт");
    @Getter
    private final String displayValue;

    UnitOfMeasure(String displayValue) {
        this.displayValue = displayValue;
    }

    public static UnitOfMeasure valueOfDisplayValue(String displayValue) {
        for (UnitOfMeasure unit : values()) {
            if (unit.displayValue.equals(displayValue)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
