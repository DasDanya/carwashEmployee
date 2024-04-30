package ru.pin120.carwashemployee.CategoriesAndServices;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoriesWithServicesDTO {

    private String categoryName;
    private ArrayList<String> servicesOfCategory;

    public CategoriesWithServicesDTO(String categoryName) {
        this.categoryName = categoryName;
    }
}
