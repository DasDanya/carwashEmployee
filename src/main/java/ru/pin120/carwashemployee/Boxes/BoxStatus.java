package ru.pin120.carwashemployee.Boxes;


import lombok.Getter;

/**
 * Перечисление BoxStatus определяет возможные статусы бокса.
 * Каждый статус имеет своё отображаемое значение на русском языке.
 */
public enum BoxStatus {


    /**
     * Бокс доступен.
     */
    AVAILABLE("Доступен"),

    /**
     * Бокс закрыт.
     */
    CLOSED("Закрыт");

    /**
     * Отображаемое значение статуса бокса.
     */
    @Getter
    private final String displayValue;

    /**
     * Конструктор BoxStatus для установки отображаемого значения.
     *
     * @param displayValue Отображаемое значение статуса бокса.
     */
    BoxStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * Метод для получения экземпляра перечисления по его отображаемому значению.
     *
     * @param displayValue Отображаемое значение статуса заказа.
     * @return Соответствующий экземпляр перечисления BoxStatus.
     * @throws IllegalArgumentException если переданное отображаемое значение не соответствует ни одной константе перечисления.
     */
    public static BoxStatus valueOfDisplayValue(String displayValue) {
        for (BoxStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }

}
