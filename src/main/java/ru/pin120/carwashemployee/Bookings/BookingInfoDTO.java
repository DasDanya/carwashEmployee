package ru.pin120.carwashemployee.Bookings;

import lombok.Getter;
import lombok.Setter;

/**
 * DTO для получения количества и стоимости заказов
 */
@Getter
@Setter
public class BookingInfoDTO {

    /**
     * общее количество заказов
     */
    private int totalCount;
    /**
     * общая стоимость заказов
     */
    private int totalPrice;
}
