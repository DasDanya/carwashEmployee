package ru.pin120.carwashemployee.Cleaners;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;
import ru.pin120.carwashemployee.WorkSchedule.WorkSchedule;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CleanerDTO {
    private Long clrId;
    private String clrSurname;
    private String clrName;
    private String clrPatronymic;
    private CleanerStatus clrStatus;
    private Box box;
    private List<WorkSchedule> workSchedules;

}
