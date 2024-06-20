package ru.pin120.carwashemployee.CategoriesOfTransport;

import lombok.*;

/**
 * Модель категории транспорта
 */
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@ToString
public class CategoryOfTransport {

    /**
     * id категории
     */
    private Long catTrId;
    /**
     * Название категории
     */
    private String catTrName;
}
