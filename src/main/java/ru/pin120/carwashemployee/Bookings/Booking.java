package ru.pin120.carwashemployee.Bookings;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Booking {

    private String bkId;
    private LocalDateTime bkStartTime;
    private LocalDateTime bkEndTime;
    private Integer bkPrice;
    private BookingStatus bkStatus;
    private Box box;
    private Cleaner cleaner;
    private ClientsTransport clientTransport;
    private List<Service> services;

}
