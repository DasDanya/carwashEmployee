package ru.pin120.carwashemployee.PriceListPosition;

import lombok.*;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PriceListPosition {

    private Long plId;
    private Integer plPrice;
    private Integer plTime;
    private Service service;
    private CategoryOfTransport categoryOfTransport;

}
