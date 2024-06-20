package ru.pin120.carwashemployee.Http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.pin120.carwashemployee.AppHelper;


import java.io.IOException;

/**
 * Класс AuthInterceptor реализует интерфейс Interceptor и предназначен для добавления в заголовок авторизации JWT токен
 */
public class AuthInterceptor implements Interceptor {

    /**
     * Перехватывает и обрабатывает цепочку запроса, добавляя заголовок "Authorization" с JWT токеном
     *
     * @param chain Цепочка запроса
     * @return Ответ от сервера
     * @throws IOException Если происходит ошибка ввода-вывода во время обработки запроса.
     */
    @Override
    public Response intercept( Chain chain) throws IOException {
        String token = AppHelper.getUserInfo().isEmpty() ? "" : AppHelper.getUserInfo().get(0);
        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder();
        builder.header("Authorization", "Bearer " + token);
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
