package ru.pin120.carwashemployee.Http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Users.UserDTO;

/**
 * ����� � ����������� � ������� � JWT ������
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    /**
     * ����� ��������������
     */
    private String type;
    /**
     * JWT �����
     */
    private String token;

    /**
     * ������ � ������������
     */
    private UserDTO userDTO;
}
