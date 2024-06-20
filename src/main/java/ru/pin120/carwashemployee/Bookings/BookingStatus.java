package ru.pin120.carwashemployee.Bookings;

import lombok.Getter;

/**
 * Статус заказа
 */
public enum BookingStatus {
    /** Статус бронирования: Бронь */
    BOOKED("Бронь"),
    /** Статус бронирования: Отменен */
    CANCELLED("Отменен"),
    /** Статус бронирования: Выполнен */
    DONE("Выполнен"),
    /** Статус бронирования: Выполняется */
    IN_PROGRESS("Выполняется"),
    /** Статус бронирования: Не выполнен */
    NOT_DONE("Не выполнен");

    /**
     * Отображаемое значение статуса заказа на русском языке
     */
    @Getter
    private final String displayValue;


    /**
     * Конструктор для установки отображаемого значения статуса заказа
     *
     * @param displayValue отображаемое значение на русском языке
     */
    BookingStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Возвращает статус заказа на основе отображаемого значения
     *
     * @param displayValue отображаемое значение на русском языке
     * @return соответствующий статус заказа
     * @throws IllegalArgumentException если отображаемое значение не соответствует ни одному из статусов
     */
    public static BookingStatus valueOfDisplayValue(String displayValue) {
        for (BookingStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
