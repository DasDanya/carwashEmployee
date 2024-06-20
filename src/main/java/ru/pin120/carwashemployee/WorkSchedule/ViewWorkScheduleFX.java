package ru.pin120.carwashemployee.WorkSchedule;

import javafx.beans.property.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * FX представление рабочего дня
 */
public class ViewWorkScheduleFX {

    private LongProperty boxId;
    private StringProperty workDay;

    public long getBoxId() {
        return boxId.get();
    }

    public LongProperty boxIdProperty() {
        return boxId;
    }

    public String getWorkDay() {
        return workDay.get();
    }

    public StringProperty workDayProperty() {
        return workDay;
    }

    public ViewWorkScheduleFX(Long boxId, LocalDate workDay) {
        this.boxId = new SimpleLongProperty(boxId);
        this.workDay = new SimpleStringProperty(workDay.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
    }
}
