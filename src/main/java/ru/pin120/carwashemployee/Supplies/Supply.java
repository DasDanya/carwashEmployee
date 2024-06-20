package ru.pin120.carwashemployee.Supplies;

import lombok.*;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;

/**
 * Модель расходного материала
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supply {

    /**
     * id расходного материала
     */
    private Long supId;
    /**
     * Название
     */
    private String supName;
    /**
     * Общее количество
     */
    private int supCount;
    /**
     * Количество/объём единицы
     */
    private int supMeasure;
    /**
     * Название фотографии
     */
    private String supPhotoName;
    /**
     * Категория
     */
    private CategoryOfSupplies category;
}
