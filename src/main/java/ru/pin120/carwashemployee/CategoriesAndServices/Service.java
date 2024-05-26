package ru.pin120.carwashemployee.CategoriesAndServices;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Service {

    private String servName;
    List<CategoryOfSupplies> categoriesOfSupplies;

    public Service(String servName) {
        this.servName = servName;
    }
}
