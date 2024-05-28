package ru.pin120.carwashemployee.Supplies;

import lombok.*;
import ru.pin120.carwashemployee.CategoriesOfSupplies.CategoryOfSupplies;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Supply {

    private Long supId;
    private String supName;
    private int supCount;
    private int supMeasure;
    private String supPhotoName;
    private CategoryOfSupplies category;
}
