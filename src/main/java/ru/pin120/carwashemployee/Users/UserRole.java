package ru.pin120.carwashemployee.Users;

import lombok.Getter;

/**
 * ������������ ����� ������������
 */
public enum UserRole {

    ADMINISTRATOR("�������������"),
    OWNER("��������");

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
        throw new IllegalArgumentException("��� ��������� ������������ � ������������ ��������� " + displayValue);
    }
}
