package ru.pin120.carwashemployee.Bookings;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO ��� ��������� ���������� � ��������� �������
 */
@Getter
@Setter
public class BookingInfoDTO {

    /**
     * ����� ���������� �������
     */
    private int totalCount;
    /**
     * ����� ��������� �������
     */
    private int totalPrice;
}
