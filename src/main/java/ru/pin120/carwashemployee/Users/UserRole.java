package ru.pin120.carwashemployee.Users;

import lombok.Getter;

/**
 * Перечисление ролей пользователя
 */
public enum UserRole {

    ADMINISTRATOR("Администратор"),
    OWNER("Владелец");

    @Getter
    private final String displayValue;

    UserRole(String displayValue) {
        this.displayValue = displayValue;
    }

    public static UserRole valueOfDisplayValue(String displayValue) {
        for (UserRole role : values()) {
            if (role.displayValue.equals(displayValue)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Нет константы перечисления с отображаемым значением " + displayValue);
    }
}
