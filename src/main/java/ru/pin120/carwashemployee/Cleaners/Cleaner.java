package ru.pin120.carwashemployee.Cleaners;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Boxes.Box;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cleaner {

    private Long clrId;
    private String clrSurname;
    private String clrName;
    private String clrPatronymic;
    private String clrPhone;
    private String clrPhotoName;
    private CleanerStatus clrStatus;
}
