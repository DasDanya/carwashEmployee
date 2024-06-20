package ru.pin120.carwashemployee.Cleaners;

import lombok.Getter;

/**
 * ������������ �������� �������
 */
public enum CleanerStatus {
    /**
     * ��������
     */
    ACT("��������"),
    /**
     * ������
     */
    DISMISSED("������");

    /**
     * ������������ �������� �������.
     */
    @Getter
    private final String displayValue;

    /**
     * ����������� � ���������� ������������� ��������.
     *
     * @param displayValue ������������ �������� �������
     */
    CleanerStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * ���������� ��������� ������������ �� ��� ������������� ��������.
     *
     * @param displayValue ������������ �������� �������
     * @return ��������������� ��������� ������������
     * @throws IllegalArgumentException ���� ���������� ������������ �������� �� ������������� �� ������ �������
     */
    public static CleanerStatus valueOfDisplayValue(String displayValue) {
        for (CleanerStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("��� ��������� ������������ � ������������ ��������� " + displayValue);
    }
}
