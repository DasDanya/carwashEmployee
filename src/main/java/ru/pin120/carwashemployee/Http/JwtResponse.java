package ru.pin120.carwashemployee.Http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.pin120.carwashemployee.Users.UserDTO;

/**
 * Класс с информацией о клиенте и JWT токене
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class JwtResponse {
    /**
     * Схема аутентификации
     */
    private String type;
    /**
     * JWT токен
     */
    private String token;

    /**
     * Данные о пользователе
     */
    private UserDTO userDTO;
}
