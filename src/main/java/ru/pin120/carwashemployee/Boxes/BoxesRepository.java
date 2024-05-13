package ru.pin120.carwashemployee.Boxes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Transport.Transport;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

public class BoxesRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/boxes";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    public List<Box> getAll() throws Exception {

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Box>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public void create(Box box) throws Exception{
        String jsonData = gson.toJson(box);
        System.out.println(jsonData);
    }
}
