package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO услуги
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceDTO {

    /**
     * Название услуги
     */
    private String servName;
    /**
     * Название категории услуги
     */
    private String catName;
}
