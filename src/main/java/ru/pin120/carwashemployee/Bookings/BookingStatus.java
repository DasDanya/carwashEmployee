package ru.pin120.carwashemployee.Bookings;

import lombok.Getter;

public enum BookingStatus {

    BOOKED("Бронь"),
    CANCELLED_BY_CLIENT("Отменен клиентом"),
    DONE("Выполнен"),
    IN_PROGRESS("Выполняется"),
    NOT_DONE("Не выполнен");

    @Getter
    private final String displayValue;

    BookingStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public static BookingStatus valueOfDisplayValue(String displayValue) {
        for (BookingStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
