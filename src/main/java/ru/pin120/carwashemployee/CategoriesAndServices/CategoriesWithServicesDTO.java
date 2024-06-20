package ru.pin120.carwashemployee.CategoriesAndServices;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO категории вместе с услугами
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesWithServicesDTO {

    /**
     * Название категории
     */
    private String categoryName;
    /**
     * Список названий услуг данной категории
     */
    private ArrayList<String> servicesOfCategory;

//    //public CategoriesWithServicesDTO(String categoryName) {
//        this.categoryName = categoryName;
//    }
}
