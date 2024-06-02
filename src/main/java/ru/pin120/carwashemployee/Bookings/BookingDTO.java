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

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BookingDTO {
    private String bkId;
    private BookingStatus bkStatus;
    private LocalDateTime bkStartTime;
    private LocalDateTime bkEndTime;
    private Box box;
    private Cleaner cleaner;
    private ClientsTransport clientTransport;
    private List<ServiceWithPriceList> services;
}
