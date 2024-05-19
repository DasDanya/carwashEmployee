package ru.pin120.carwashemployee.Cleaners;

import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.BoxStatus;

public enum CleanerStatus {
    WORKING("Работает"),
    DISMISSED("Уволен");

    @Getter
    private final String displayValue;

    CleanerStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public static CleanerStatus valueOfDisplayValue(String displayValue) {
        for (CleanerStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}