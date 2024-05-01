package ru.pin120.carwashemployee.CategoriesAndServices;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOfServices {
    private String catName;
    private List<Service> services;
}
