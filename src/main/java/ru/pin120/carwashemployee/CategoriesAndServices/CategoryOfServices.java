package ru.pin120.carwashemployee.CategoriesAndServices;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Модель категории услуг
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOfServices {
    /**
     * Название категории
     */
    private String catName;
    /**
     * Список услуг
     */
    private List<Service> services;
}
