package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BindWithCategoryDTO {
    private String parameter;
    private String catNameToBind;
}
