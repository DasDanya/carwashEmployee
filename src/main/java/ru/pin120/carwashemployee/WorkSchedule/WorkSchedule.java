package ru.pin120.carwashemployee.WorkSchedule;

import lombok.*;
import ru.pin120.carwashemployee.Cleaners.Cleaner;

import java.time.LocalDate;
import java.time.LocalTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class WorkSchedule {

    private Long wsId;
    private LocalDate wsWorkDay;
}
