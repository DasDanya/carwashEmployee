package ru.pin120.carwashemployee.Bookings;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;
import ru.pin120.carwashemployee.PriceListPosition.ServiceWithPriceList;

import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO заказа
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    /**
     * id заказа
     */
    private String bkId;
    /**
     * статус
     */
    private BookingStatus bkStatus;
    /**
     * время начала
     */
    private LocalDateTime bkStartTime;
    /**
     * время окончания
     */
    private LocalDateTime bkEndTime;
    /**
     * бокс, в котором выполняется заказ
     */
    private Box box;
    /**
     * мойщик,выполняющий заказ
     */
    private Cleaner cleaner;
    /**
     * транспорт клиента
     */
    private ClientsTransport clientTransport;
    /**
     * список услуг с их стоимостью и временем выполнения
     */
    private List<ServiceWithPriceList> services;
}
