package ru.pin120.carwashemployee.Cleaners;

import lombok.Getter;

/**
 * Перечисление статусов мойщика
 */
public enum CleanerStatus {
    /**
     * Работает
     */
    ACT("Работает"),
    /**
     * Уволен
     */
    DISMISSED("Уволен");

    /**
     * Отображаемое значение статуса.
     */
    @Getter
    private final String displayValue;

    /**
     * Конструктор с параметром отображаемого значения.
     *
     * @param displayValue отображаемое значение статуса
     */
    CleanerStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Возвращает экземпляр перечисления по его отображаемому значению.
     *
     * @param displayValue отображаемое значение статуса
     * @return соответствующий экземпляр перечисления
     * @throws IllegalArgumentException если переданное отображаемое значение не соответствует ни одному статусу
     */
    public static CleanerStatus valueOfDisplayValue(String displayValue) {
        for (CleanerStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
