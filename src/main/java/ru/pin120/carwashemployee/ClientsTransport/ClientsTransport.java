package ru.pin120.carwashemployee.ClientsTransport;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Transport.Transport;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ClientsTransport {

    private Long clTrId;
    private String clTrStateNumber;
    private Transport transport;
    private Client client;
}
