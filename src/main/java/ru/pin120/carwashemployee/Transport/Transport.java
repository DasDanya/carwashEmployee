package ru.pin120.carwashemployee.Transport;

import lombok.*;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;

/**
 * Модель транспорта
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Transport {

    private Long trId;
    private String trMark;
    private String trModel;
    private CategoryOfTransport categoryOfTransport;

}
