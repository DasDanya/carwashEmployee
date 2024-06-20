package ru.pin120.carwashemployee.Cleaners;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.WorkSchedule.WorkSchedule;

import java.util.List;

/**
 * DTO мойщика
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CleanerDTO {
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
     * Статус
     */
    private CleanerStatus clrStatus;
    /**
     * Бокс
     */
    private Box box;
    /**
     * Список рабочих дней
     */
    private List<WorkSchedule> workSchedules;

}
