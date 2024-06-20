package ru.pin120.carwashemployee.Clients;

import lombok.*;

/**
 * Модель клиента
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Client {

    /**
     * id клиента
     */
    private Long clId;
    /**
     * Фамилия
     */
    private String clSurname;
    /**
     * Имя
     */
    private String clName;
    /**
     * Номер телефона
     */
    private String clPhone;
    /**
     * Скидка
     */
    private Integer clDiscount;
}
