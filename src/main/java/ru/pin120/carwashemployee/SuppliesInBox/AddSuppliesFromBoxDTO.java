package ru.pin120.carwashemployee.SuppliesInBox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO добавления расходного материала
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AddSuppliesFromBoxDTO {
    private SuppliesInBox suppliesInBox;
    private int countOfAdded;

}
