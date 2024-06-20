package ru.pin120.carwashemployee.CategoriesOfSupplies;

import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.BoxStatus;

/**
 * Перечисление единиц измерения
 */
public enum UnitOfMeasure {
    /**
     * Миллилитры
     */
    MILLILITERS("мл"),
    /**
     * Штуки
     */
    PIECE("шт");

    /**
     * Отображаемое значение единицы измерения.
     */
    @Getter
    private final String displayValue;

    /**
     * Конструктор единицы измерения с заданным отображаемым значением.
     *
     * @param displayValue Отображаемое значение единицы измерения.
     */
    UnitOfMeasure(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Возвращает объект {@code UnitOfMeasure} на основе его отображаемого значения.
     *
     * @param displayValue Отображаемое значение единицы измерения.
     * @return Объект {@code UnitOfMeasure}, соответствующий заданному отображаемому значению.
     * @throws IllegalArgumentException Если отображаемое значение не соответствует ни одной константе перечисления.
     */
    public static UnitOfMeasure valueOfDisplayValue(String displayValue) {
        for (UnitOfMeasure unit : values()) {
            if (unit.displayValue.equals(displayValue)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
