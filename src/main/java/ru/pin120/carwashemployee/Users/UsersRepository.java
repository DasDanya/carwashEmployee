package ru.pin120.carwashemployee.Users;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.extern.java.Log;
import okhttp3.*;
import ru.pin120.carwashemployee.Aes;
import ru.pin120.carwashemployee.AppHelper;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UsersRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/users";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

    public List<User> get() throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(response.body().string(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<UserDTO>>(){}.getType();
        List<UserDTO> userDTOS = gson.fromJson(jsonData, type);

        List<User> users = new ArrayList<>();
        for(UserDTO userDTO: userDTOS){
            users.add(convertToUser(userDTO));
        }

        return users;
    }

    public User register(RegisterRequest registerRequest) throws Exception {
        registerRequest.setUsername(Aes.encrypt(registerRequest.getUsername()));
        registerRequest.setPassword(Aes.encrypt(registerRequest.getPassword()));
        registerRequest.setRole(Aes.encrypt(registerRequest.getRole()));

        User createdUser = null;
        String jsonData = gson.toJson(registerRequest);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/register")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<JwtResponse>() {}.getType();
            JwtResponse jwtResponse = gson.fromJson(result, type);
            createdUser = convertToUser(jwtResponse.getUserDTO());
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return createdUser;
    }

    public boolean login(LoginRequest loginRequest) throws Exception {
        boolean successLogin = false;

        loginRequest.setUsername(Aes.encrypt(loginRequest.getUsername()));
        loginRequest.setPassword(Aes.encrypt(loginRequest.getPassword()));

        String jsonData = gson.toJson(loginRequest);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/login")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<JwtResponse>() {}.getType();

            JwtResponse jwtResponse = gson.fromJson(result, type);
            addUserInfo(jwtResponse);

            successLogin = true;
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return successLogin;
    }

    private User convertToUser(UserDTO userDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = new User();
        user.setUsId(Long.valueOf(Aes.decrypt(userDTO.getUsId())));
        user.setUsName(Aes.decrypt(userDTO.getUsName()));
        user.setUsRole(UserRole.valueOf(Aes.decrypt(userDTO.getUsRole())));

        return user;
    }

    public void addUserInfo(JwtResponse jwtResponse) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {


        String token = Aes.decrypt(jwtResponse.getToken());
        String username = Aes.decrypt(jwtResponse.getUserDTO().getUsName());
        String role = Aes.decrypt(jwtResponse.getUserDTO().getUsRole());

    }
}
