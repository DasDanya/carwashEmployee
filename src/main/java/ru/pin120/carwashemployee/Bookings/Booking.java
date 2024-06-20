package ru.pin120.carwashemployee.Bookings;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Модель заказа
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {

    /**
     * id заказа
     */
    private String bkId;
    /**
     * время начала
     */
    private LocalDateTime bkStartTime;
    /**
     * время окончания
     */
    private LocalDateTime bkEndTime;
    /**
     * стоимость
     */
    private Integer bkPrice;
    /**
     * статус
     */
    private BookingStatus bkStatus;
    /**
     * бокс, в котором выполняется заказ
     */
    private Box box;
    /**
     * мойщик, выполняющий заказ
     */
    private Cleaner cleaner;
    /**
     * транспорт клиента
     */
    private ClientsTransport clientTransport;
    /**
     * список услуг
     */
    private List<Service> services;

}
