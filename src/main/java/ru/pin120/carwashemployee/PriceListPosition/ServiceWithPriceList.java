package ru.pin120.carwashemployee.PriceListPosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Позиция прайс-листа
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceWithPriceList {
    /**
     * Название категории
     */
    private String catName;
    /**
     * Название услуги
     */
    private String servName;
    /**
     * Стоимость услуги
     */
    private Integer plPrice;
    /**
     * Время выполнения услуги
     */
    private Integer plTime;
}
