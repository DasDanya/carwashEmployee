package ru.pin120.carwashemployee.Clients;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Client {

    private Long clId;
    private String clSurname;
    private String clName;
    private String clPhone;
    private Integer clSale;
}
