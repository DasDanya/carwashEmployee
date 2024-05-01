package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class EditCategoryOrServiceDTO {

    private String pastName;
    private String newName;
}
