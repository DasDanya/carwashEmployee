package ru.pin120.carwashemployee.Bookings;

import lombok.Getter;

/**
 * ������ ������
 */
public enum BookingStatus {
    /** ������ ������������: ����� */
    BOOKED("�����"),
    /** ������ ������������: ������� */
    CANCELLED("�������"),
    /** ������ ������������: �������� */
    DONE("��������"),
    /** ������ ������������: ����������� */
    IN_PROGRESS("�����������"),
    /** ������ ������������: �� �������� */
    NOT_DONE("�� ��������");

    /**
     * ������������ �������� ������� ������ �� ������� �����
     */
    @Getter
    private final String displayValue;


    /**
     * ����������� ��� ��������� ������������� �������� ������� ������
     *
     * @param displayValue ������������ �������� �� ������� �����
     */
    BookingStatus(String displayValue) {
        this.displayValue = displayValue;
    }

    /**
     * ���������� ������ ������ �� ������ ������������� ��������
     *
     * @param displayValue ������������ �������� �� ������� �����
     * @return ��������������� ������ ������
     * @throws IllegalArgumentException ���� ������������ �������� �� ������������� �� ������ �� ��������
     */
    public static BookingStatus valueOfDisplayValue(String displayValue) {
        for (BookingStatus status : values()) {
            if (status.displayValue.equals(displayValue)) {
                return status;
            }
        }
        throw new IllegalArgumentException("��� ��������� ������������ � ������������ ��������� " + displayValue);
    }
}
