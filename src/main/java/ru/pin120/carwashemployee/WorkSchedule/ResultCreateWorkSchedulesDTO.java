package ru.pin120.carwashemployee.WorkSchedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * DTO с результатом добавления рабочих дней
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResultCreateWorkSchedulesDTO {
    private String conflictMessage;
    private List<WorkSchedule> createdWorkSchedules;
}
