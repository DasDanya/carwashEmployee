package ru.pin120.carwashemployee.CategoriesOfSupplies;

import lombok.*;

/**
 * Модель категории расходных материалов
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CategoryOfSupplies {
    /**
     * Название категории
     */
    private String csupName;
    /**
     * Единица измерения
     */
    private UnitOfMeasure unit;
}
