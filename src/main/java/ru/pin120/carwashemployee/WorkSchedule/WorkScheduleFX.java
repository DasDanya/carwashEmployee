package ru.pin120.carwashemployee.WorkSchedule;

import javafx.beans.property.*;
import javafx.scene.control.CheckBox;
import ru.pin120.carwashemployee.AppHelper;

import java.time.LocalTime;

public class WorkScheduleFX {

    private LongProperty clrId;
    private StringProperty fio;
    private StringProperty status;
    private StringProperty phone;
    private ObjectProperty<CheckBox> day1 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day2 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day3 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day4 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day5 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day6 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day7 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day8 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day9 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day10 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day11 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day12 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day13 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day14 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day15 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day16 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day17 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day18 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day19 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day20 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day21 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day22 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day23 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day24 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day25 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day26 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day27 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day28 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day29 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day30 = new SimpleObjectProperty<>(new CheckBox());
    private ObjectProperty<CheckBox> day31 = new SimpleObjectProperty<>(new CheckBox());

    public long getClrId() {
        return clrId.get();
    }

    public LongProperty clrIdProperty() {
        return clrId;
    }

    public String getFio() {
        return fio.get();
    }

    public StringProperty fioProperty() {
        return fio;
    }

    public String getStatus() {
        return status.get();
    }

    public StringProperty statusProperty() {
        return status;
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public CheckBox getDay1() {
        return day1.get();
    }

    public ObjectProperty<CheckBox> day1Property() {
        return day1;
    }

    public CheckBox getDay2() {
        return day2.get();
    }

    public ObjectProperty<CheckBox> day2Property() {
        return day2;
    }

    public CheckBox getDay3() {
        return day3.get();
    }

    public ObjectProperty<CheckBox> day3Property() {
        return day3;
    }

    public CheckBox getDay4() {
        return day4.get();
    }

    public ObjectProperty<CheckBox> day4Property() {
        return day4;
    }

    public CheckBox getDay5() {
        return day5.get();
    }

    public ObjectProperty<CheckBox> day5Property() {
        return day5;
    }

    public CheckBox getDay6() {
        return day6.get();
    }

    public ObjectProperty<CheckBox> day6Property() {
        return day6;
    }

    public CheckBox getDay7() {
        return day7.get();
    }

    public ObjectProperty<CheckBox> day7Property() {
        return day7;
    }

    public CheckBox getDay8() {
        return day8.get();
    }

    public ObjectProperty<CheckBox> day8Property() {
        return day8;
    }

    public CheckBox getDay9() {
        return day9.get();
    }

    public ObjectProperty<CheckBox> day9Property() {
        return day9;
    }

    public CheckBox getDay10() {
        return day10.get();
    }

    public ObjectProperty<CheckBox> day10Property() {
        return day10;
    }

    public CheckBox getDay11() {
        return day11.get();
    }

    public ObjectProperty<CheckBox> day11Property() {
        return day11;
    }

    public CheckBox getDay12() {
        return day12.get();
    }

    public ObjectProperty<CheckBox> day12Property() {
        return day12;
    }

    public CheckBox getDay13() {
        return day13.get();
    }

    public ObjectProperty<CheckBox> day13Property() {
        return day13;
    }

    public CheckBox getDay14() {
        return day14.get();
    }

    public ObjectProperty<CheckBox> day14Property() {
        return day14;
    }

    public CheckBox getDay15() {
        return day15.get();
    }

    public ObjectProperty<CheckBox> day15Property() {
        return day15;
    }

    public CheckBox getDay16() {
        return day16.get();
    }

    public ObjectProperty<CheckBox> day16Property() {
        return day16;
    }

    public CheckBox getDay17() {
        return day17.get();
    }

    public ObjectProperty<CheckBox> day17Property() {
        return day17;
    }

    public CheckBox getDay18() {
        return day18.get();
    }

    public ObjectProperty<CheckBox> day18Property() {
        return day18;
    }

    public CheckBox getDay19() {
        return day19.get();
    }

    public ObjectProperty<CheckBox> day19Property() {
        return day19;
    }

    public CheckBox getDay20() {
        return day20.get();
    }

    public ObjectProperty<CheckBox> day20Property() {
        return day20;
    }

    public CheckBox getDay21() {
        return day21.get();
    }

    public ObjectProperty<CheckBox> day21Property() {
        return day21;
    }

    public CheckBox getDay22() {
        return day22.get();
    }

    public ObjectProperty<CheckBox> day22Property() {
        return day22;
    }

    public CheckBox getDay23() {
        return day23.get();
    }

    public ObjectProperty<CheckBox> day23Property() {
        return day23;
    }

    public CheckBox getDay24() {
        return day24.get();
    }

    public ObjectProperty<CheckBox> day24Property() {
        return day24;
    }

    public CheckBox getDay25() {
        return day25.get();
    }

    public ObjectProperty<CheckBox> day25Property() {
        return day25;
    }

    public CheckBox getDay26() {
        return day26.get();
    }

    public ObjectProperty<CheckBox> day26Property() {
        return day26;
    }

    public CheckBox getDay27() {
        return day27.get();
    }

    public ObjectProperty<CheckBox> day27Property() {
        return day27;
    }

    public CheckBox getDay28() {
        return day28.get();
    }

    public ObjectProperty<CheckBox> day28Property() {
        return day28;
    }

    public CheckBox getDay29() {
        return day29.get();
    }

    public ObjectProperty<CheckBox> day29Property() {
        return day29;
    }

    public CheckBox getDay30() {
        return day30.get();
    }

    public ObjectProperty<CheckBox> day30Property() {
        return day30;
    }

    public CheckBox getDay31() {
        return day31.get();
    }

    public ObjectProperty<CheckBox> day31Property() {
        return day31;
    }

    public WorkScheduleFX(Long clrId, String fio, String status, String phone){
        this.clrId = new SimpleLongProperty(clrId);
        this.fio = new SimpleStringProperty(fio);
        this.status = new SimpleStringProperty(status);
        this.phone = new SimpleStringProperty(phone);
    }

    public WorkScheduleFX(Long clrId, String fio,CheckBox day1, CheckBox day2, CheckBox day3, CheckBox day4, CheckBox day5, CheckBox day6, CheckBox day7, CheckBox day8, CheckBox day9, CheckBox day10, CheckBox day11, CheckBox day12, CheckBox day13, CheckBox day14, CheckBox day15, CheckBox day16, CheckBox day17, CheckBox day18, CheckBox day19, CheckBox day20, CheckBox day21, CheckBox day22, CheckBox day23, CheckBox day24, CheckBox day25, CheckBox day26, CheckBox day27, CheckBox day28, CheckBox day29, CheckBox day30, CheckBox day31) {
        this.clrId = new SimpleLongProperty(clrId);
        this.fio = new SimpleStringProperty(fio);
        this.day1 = new SimpleObjectProperty<>(day1);
        this.day2 = new SimpleObjectProperty<>(day2);
        this.day3 = new SimpleObjectProperty<>(day3);
        this.day4 = new SimpleObjectProperty<>(day4);
        this.day5 = new SimpleObjectProperty<>(day5);
        this.day6 = new SimpleObjectProperty<>(day6);
        this.day7 = new SimpleObjectProperty<>(day7);
        this.day8 = new SimpleObjectProperty<>(day8);
        this.day9 = new SimpleObjectProperty<>(day9);
        this.day10 = new SimpleObjectProperty<>(day10);
        this.day11 = new SimpleObjectProperty<>(day11);
        this.day12 = new SimpleObjectProperty<>(day12);
        this.day13 = new SimpleObjectProperty<>(day13);
        this.day14 = new SimpleObjectProperty<>(day14);
        this.day15 = new SimpleObjectProperty<>(day15);
        this.day16 = new SimpleObjectProperty<>(day16);
        this.day17 = new SimpleObjectProperty<>(day17);
        this.day18 = new SimpleObjectProperty<>(day18);
        this.day19 = new SimpleObjectProperty<>(day19);
        this.day20 = new SimpleObjectProperty<>(day20);
        this.day21 = new SimpleObjectProperty<>(day21);
        this.day22 = new SimpleObjectProperty<>(day22);
        this.day23 = new SimpleObjectProperty<>(day23);
        this.day24 = new SimpleObjectProperty<>(day24);
        this.day25 = new SimpleObjectProperty<>(day25);
        this.day26 = new SimpleObjectProperty<>(day26);
        this.day27 = new SimpleObjectProperty<>(day27);
        this.day28 = new SimpleObjectProperty<>(day28);
        this.day29 = new SimpleObjectProperty<>(day29);
        this.day30 = new SimpleObjectProperty<>(day30);
        this.day31 = new SimpleObjectProperty<>(day31);
    }
}
