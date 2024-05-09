package ru.pin120.carwashemployee.Transport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPosition;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

public class TransportRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/transport";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    public List<Transport> getByPage(int pageIndex) throws Exception {
        if(pageIndex < 0){
            return new ArrayList<>();
        }
        Request request = new Request.Builder()
                .url(url+"?pageIndex="+pageIndex)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Transport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public Transport create(Transport transport) throws Exception {
        Transport createdTransport = null;
        String jsonData = gson.toJson(transport);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<Transport>(){}.getType();
                createdTransport = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(response.body().string(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdTransport;
    }

    public Transport edit(Transport transport) throws Exception {
        Transport editedTransport = null;
        String jsonData = gson.toJson(transport);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+transport.getTrId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<Transport>(){}.getType();
                editedTransport = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(response.body().string(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedTransport;
    }


    public boolean delete(Long id) throws Exception {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/"+id)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 204:
                successDelete = true;
                break;
            case 400:
                throw new HttpRetryException(response.body().string(), 400);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successDelete;
    }
}
