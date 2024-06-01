package ru.pin120.carwashemployee.Cleaners;

import lombok.*;
import ru.pin120.carwashemployee.Boxes.Box;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Cleaner {

    private Long clrId;
    private String clrSurname;
    private String clrName;
    private String clrPatronymic;
    private String clrPhone;
    private String clrPhotoName;
    private CleanerStatus clrStatus;
}
