package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * DTO для привязки услуг к категории
 */
@AllArgsConstructor
@Getter
@Setter
public class BindWithCategoryDTO {
    /**
     * Параметр (услуга или категория)
     */
    private String parameter;
    /**
     * Категория, к которой происходит привязка
     */
    private String catNameToBind;
}
