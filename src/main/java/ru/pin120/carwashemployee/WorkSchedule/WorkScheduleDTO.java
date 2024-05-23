package ru.pin120.carwashemployee.WorkSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@AllArgsConstructor
@Getter
public class WorkScheduleDTO {

    private Long clrId;
    private List<WorkSchedule> workSchedules;
}
