package ru.pin120.carwashemployee.CategoriesOfSupplies;

import lombok.Getter;
import ru.pin120.carwashemployee.Boxes.BoxStatus;

/**
 * ������������ ������ ���������
 */
public enum UnitOfMeasure {
    /**
     * ����������
     */
    MILLILITERS("��"),
    /**
     * �����
     */
    PIECE("��");

    /**
     * ������������ �������� ������� ���������.
     */
    @Getter
    private final String displayValue;

    /**
     * ����������� ������� ��������� � �������� ������������ ���������.
     *
     * @param displayValue ������������ �������� ������� ���������.
     */
    UnitOfMeasure(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * ���������� ������ {@code UnitOfMeasure} �� ������ ��� ������������� ��������.
     *
     * @param displayValue ������������ �������� ������� ���������.
     * @return ������ {@code UnitOfMeasure}, ��������������� ��������� ������������� ��������.
     * @throws IllegalArgumentException ���� ������������ �������� �� ������������� �� ����� ��������� ������������.
     */
    public static UnitOfMeasure valueOfDisplayValue(String displayValue) {
        for (UnitOfMeasure unit : values()) {
            if (unit.displayValue.equals(displayValue)) {
                return unit;
            }
        }
        throw new IllegalArgumentException("��� ��������� ������������ � ������������ ��������� " + displayValue);
    }
}
