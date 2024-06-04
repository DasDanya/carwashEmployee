package ru.pin120.carwashemployee.Users;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {
    private Long usId;
    private String usName;
    private UserRole usRole;
}
