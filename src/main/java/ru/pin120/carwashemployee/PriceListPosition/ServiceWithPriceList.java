package ru.pin120.carwashemployee.PriceListPosition;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ServiceWithPriceList {
    private String catName;
    private String servName;
    private Integer plPrice;
    private Integer plTime;
}
