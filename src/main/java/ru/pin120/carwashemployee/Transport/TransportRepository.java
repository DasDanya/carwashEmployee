package ru.pin120.carwashemployee.Transport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.PriceListPosition.PriceListPosition;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class TransportRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/transport";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
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

    /**
     * Метод добавления транспорта
     * @param transport Создаваемый транспорт
     * @return Созданный транспорт с id
     * @throws IOException Ошибка обработки ответа от сервера
     */
    public Transport create(Transport transport) throws IOException {
        Transport createdTransport;
        String jsonData = gson.toJson(transport); // Преобразование данных в JSON

        RequestBody body = RequestBody.create(JSON, jsonData); // Создание тела запроса

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build(); // Формирование запроса

        Response response = client.newCall(request).execute(); // Получение ответа от сервера
        String result = response.body().string(); // Получение JSON
        Type type = new TypeToken<Transport>() {}.getType(); // Указание нужного типа данных
        createdTransport = gson.fromJson(result, type); // Преобразование в нужный тип данных
//        if (response.code() == 200) {
//            String result = response.body().string(); // Получение JSON
//            Type type = new TypeToken<Transport>() {}.getType(); // Указание нужного типа данных
//            createdTransport = gson.fromJson(result, type); // Преобразование в нужный тип данных
//        } else {
//            throw new HttpRetryException(response.body().string(), response.code());
//        }

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
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Transport>() {}.getType();
            editedTransport = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
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


    public List<Transport> search(int pageIndex, String category, String mark, String model) throws Exception {
        if (pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = "?pageIndex=" + pageIndex;
        if(category != null && !category.isBlank()){
            category = URLEncoder.encode(category, "UTF-8");
            partUrl+="&category=" + category;
        }
        if(mark != null && !mark.isBlank()){
            mark = URLEncoder.encode(mark, "UTF-8");
            partUrl+="&mark=" + mark;
        }
        if(model != null && !model.isBlank()){
            model = URLEncoder.encode(model, "UTF-8");
            partUrl+="&model=" + model;
        }

        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Transport>>(){}.getType();

        return gson.fromJson(jsonData, type);

    }
}
