package ru.pin120.carwashemployee.ClientsTransport;

import lombok.*;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Transport.Transport;

/**
 * Модель транспорта клиента
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ClientsTransport {
    /**
     * id транспорта клиента
     */
    private Long clTrId;
    /**
     * Госномер транспорта
     */
    private String clTrStateNumber;
    /**
     * Транспорт
     */
    private Transport transport;
    /**
     * Клиент
     */
    private Client client;
}
