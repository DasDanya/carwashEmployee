package ru.pin120.carwashemployee.Boxes;


import lombok.Getter;

/**
 * ������������ BoxStatus ���������� ��������� ������� �����.
 * ������ ������ ����� ��� ������������ �������� �� ������� �����.
 */
public enum BoxStatus {


    /**
     * ���� ��������.
     */
    AVAILABLE("��������"),

    /**
     * ���� ������.
     */
    CLOSED("������");

    /**
     * ������������ �������� ������� �����.
     */
    @Getter
    private final String displayValue;

    /**
     * ����������� BoxStatus ��� ��������� ������������� ��������.
     *
     * @param displayValue ������������ �������� ������� �����.
     */
    BoxStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * ����� ��� ��������� ���������� ������������ �� ��� ������������� ��������.
     *
     * @param displayValue ������������ �������� ������� ������.
     * @return ��������������� ��������� ������������ BoxStatus.
     * @throws IllegalArgumentException ���� ���������� ������������ �������� �� ������������� �� ����� ��������� ������������.
     */
    public static BoxStatus valueOfDisplayValue(String displayValue) {
        for (BoxStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("��� ��������� ������������ � ������������ ��������� " + displayValue);
    }

}
