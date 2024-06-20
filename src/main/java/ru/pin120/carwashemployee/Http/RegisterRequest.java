package ru.pin120.carwashemployee.Http;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * Запрос на регистрацию
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterRequest {

    /**
     * Имя пользователя
     */
    private String username;
    /**
     * Пароль
     */
    private String password;
    /**
     * Роль
     */
    private String role;
}
