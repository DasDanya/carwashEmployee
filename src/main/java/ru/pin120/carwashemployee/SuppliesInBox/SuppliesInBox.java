package ru.pin120.carwashemployee.SuppliesInBox;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Supplies.Supply;

/**
 * Модель расходного материала в боксе
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class SuppliesInBox {

    private Long sibId;
    private int countSupplies;
    private Box box;
    private Supply supply;
}
