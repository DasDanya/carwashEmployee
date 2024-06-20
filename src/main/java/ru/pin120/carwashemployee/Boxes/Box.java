package ru.pin120.carwashemployee.Boxes;

import lombok.*;

/**
 * Модель бокса
 */
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Box {

    /**
     * Номер бокса
     */
    private Long boxId;
    /**
     * Статус бокса
     */
    private BoxStatus boxStatus;
}
