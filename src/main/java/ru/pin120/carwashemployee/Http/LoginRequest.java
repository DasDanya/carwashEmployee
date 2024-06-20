package ru.pin120.carwashemployee.Http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Запрос на авторизацию
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    /**
     * Имя пользователя
     */
    private String username;

    /**
     * Пароль
     */
    private String password;
}
