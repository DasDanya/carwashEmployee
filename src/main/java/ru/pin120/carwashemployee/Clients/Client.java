package ru.pin120.carwashemployee.Clients;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Client {

    private Long clId;
    private String clSurname;
    private String clName;
    private String clPhone;
    private Integer clDiscount;
}
