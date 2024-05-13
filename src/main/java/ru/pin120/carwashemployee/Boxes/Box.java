package ru.pin120.carwashemployee.Boxes;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
public class Box {

    private Long boxId;
    private BoxStatus boxStatus;
}
