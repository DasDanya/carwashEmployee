package ru.pin120.carwashemployee.Cleaners;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;

/**
 * Модель мойщика
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cleaner {

    /**
     * id мойщика
     */
    private Long clrId;
    /**
     * Фамилия
     */
    private String clrSurname;
    /**
     * Имя
     */
    private String clrName;
    /**
     * Отчество
     */
    private String clrPatronymic;
    /**
     * Номер телефона
     */
    private String clrPhone;
    /**
     * Название фотографии
     */
    private String clrPhotoName;
    /**
     * Статус
     */
    private CleanerStatus clrStatus;
}
