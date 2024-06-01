package ru.pin120.carwashemployee.ClientsTransport;

import lombok.*;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Transport.Transport;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientsTransport {

    private Long clTrId;
    private String clTrStateNumber;
    private Transport transport;
    private Client client;
}
