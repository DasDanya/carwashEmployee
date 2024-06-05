package ru.pin120.carwashemployee.Users;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.Aes;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.Http.JwtResponse;
import ru.pin120.carwashemployee.Http.LoginRequest;
import ru.pin120.carwashemployee.Http.RegisterRequest;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class UsersRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/users";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient clientWithoutInterceptor = new OkHttpClient();

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();

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

        Response response = clientWithoutInterceptor.newCall(request).execute();
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

        Response response = clientWithoutInterceptor.newCall(request).execute();
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

    public boolean editPassword(String username,String password) throws Exception {
        boolean successEdit = false;
        System.out.println(AppHelper.getUserInfo().get(0));
        password = Aes.encrypt(password);
        PasswordWrapper passwordWrapper = new PasswordWrapper(password);
        String jsonData = gson.toJson(passwordWrapper);

        RequestBody body = RequestBody.create(JSON, jsonData);
        username = URLEncoder.encode(username, StandardCharsets.UTF_8);

        Request request = new Request.Builder()
                .url(url + "/editPassword/"+username)
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() == 200){
            successEdit = true;
//            String result = response.body().string();
//            Type type = new TypeToken<JwtResponse>() {}.getType();
//            JwtResponse jwtResponse = gson.fromJson(result, type);
//            String usernameCurrentUser = Aes.decrypt(jwtResponse.getUserDTO().getUsName());
//            if(usernameCurrentUser.equals(AppHelper.getUserInfo().get(1))){
//                String newToken = Aes.decrypt(jwtResponse.getToken());
//                System.out.println(newToken);
//            }
        }else{
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return successEdit;
    }

    public boolean delete(String usName) throws Exception{
        boolean successDelete;

        usName = URLEncoder.encode(usName, StandardCharsets.UTF_8);
        Request request = new Request.Builder()
                .url(url + "/delete/"+usName)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 204) {
            successDelete = true;
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return successDelete;
    }

    private User convertToUser(UserDTO userDTO) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        User user = new User();
        user.setUsId(Long.valueOf(Aes.decrypt(userDTO.getUsId())));
        user.setUsName(Aes.decrypt(userDTO.getUsName()));
        user.setUsRole(UserRole.valueOf(Aes.decrypt(userDTO.getUsRole())));

        return user;
    }

    public void addUserInfo(JwtResponse jwtResponse) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        //AppHelper.deleteTempInfoFiles();
        //AppHelper.createInfoFile();

        String token = Aes.decrypt(jwtResponse.getToken());
        String username = Aes.decrypt(jwtResponse.getUserDTO().getUsName());
        String role = Aes.decrypt(jwtResponse.getUserDTO().getUsRole());
        List<String> data = List.of(token,username,role);
        AppHelper.setUserInfo(data);

        //AppHelper.writeInInfoFile(data);
    }

}
