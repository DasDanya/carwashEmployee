package ru.pin120.carwashemployee.WorkSchedule;

import lombok.*;

import java.time.LocalDate;

/**
 * Модель рабочего дня
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkSchedule {

    private Long wsId;
    private LocalDate wsWorkDay;
}
