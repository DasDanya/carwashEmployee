package ru.pin120.carwashemployee.WorkSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.Cleaners.Cleaner;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class WorkScheduleDTO {
    private Long wsId;
    private LocalDate wsWorkDay;
    private Cleaner cleaner;
    private Box box;
}
