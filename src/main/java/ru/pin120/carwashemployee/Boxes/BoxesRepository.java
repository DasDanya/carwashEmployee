package ru.pin120.carwashemployee.Boxes;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.Transport.Transport;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий бокса
 */
public class BoxesRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/boxes";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();


    /**
     * Получение всех боксов
     * @return Список с боксами
     * @throws Exception Если возникает ошибка при выполнении запроса или обработки ответа.
     */
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

//    public List<Box> getAvailable() throws Exception {
//
//        Request request = new Request.Builder()
//                .url(url+"/available")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if(response.code() != 200){
//            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
//        }
//        String jsonData = response.body().string();
//        Type type = new TypeToken<List<Box>>(){}.getType();
//
//        return gson.fromJson(jsonData, type);
//    }



    /**
     * Создает новый бокс на сервере, используя HTTP POST запрос на URL '/boxes/create'.
     *
     * @param box Объект Box, который необходимо создать.
     * @return Объект Box, представляющий созданный бокс.
     * @throws Exception Если возникает ошибка при выполнении запроса или обработки ответа.
     */
    public Box create(Box box) throws Exception{
        Box createdBox = null;
        String jsonData = gson.toJson(box);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Box>() {}.getType();
            createdBox = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdBox;
    }

    /**
     * Изменяет существующий бокс на сервере, используя HTTP PUT запрос на URL '/boxes/edit/{id}'.
     *
     * @param box Объект Box, который необходимо изменить.
     * @return Объект Box, представляющий измененный бокс.
     * @throws Exception Если возникает ошибка при выполнении запроса или обработки ответа.
     */
    public Box edit(Box box) throws Exception{
        Box editedBox = null;
        String jsonData = gson.toJson(box);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/" + box.getBoxId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Box>() {}.getType();
            editedBox = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return editedBox;
    }

    /**
     * Удаляет существующий бокс на сервере, используя HTTP DELETE запрос на URL '/boxes/delete/{id}'.
     *
     * @param id Идентификатор бокса, который необходимо удалить.
     * @return true, если удаление выполнено успешно (HTTP статус 204), иначе false.
     * @throws Exception Если возникает ошибка при выполнении запроса или обработки ответа.
     */
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
