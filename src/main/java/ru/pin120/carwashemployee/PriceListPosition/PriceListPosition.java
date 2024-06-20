package ru.pin120.carwashemployee.PriceListPosition;

import lombok.*;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;

/**
 * Модель позиции прайс-листа
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PriceListPosition {

    /**
     * id позиции
     */
    private Long plId;
    /**
     * стоимость
     */
    private Integer plPrice;
    /**
     * время выполнения
     */
    private Integer plTime;
    /**
     * услуга
     */
    private Service service;
    /**
     * категория транспорта
     */
    private CategoryOfTransport categoryOfTransport;

}
