package ru.pin120.carwashemployee.Http;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import ru.pin120.carwashemployee.AppHelper;


import java.io.IOException;

public class AuthInterceptor implements Interceptor {

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
