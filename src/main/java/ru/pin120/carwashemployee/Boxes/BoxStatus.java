package ru.pin120.carwashemployee.Boxes;


import lombok.Getter;

public enum BoxStatus {

    AVAILABLE("Доступен"),
    REPAIR("Закрыт");
    @Getter
    private final String displayValue;

    BoxStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    public static BoxStatus valueOfDisplayValue(String displayValue) {
        for (BoxStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }

//    public static BoxStatus valueOfDisplayValue(String displayValue) {
//        for (BoxStatus status : values()) {
//            if (status.displayValue.equals(displayValue)) {
//                return status;
//            }
//        }
//        throw new IllegalArgumentException("No enum constant with displayValue: " + displayValue);
//    }
}
