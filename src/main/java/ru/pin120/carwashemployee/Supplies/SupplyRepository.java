package ru.pin120.carwashemployee.Supplies;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Http.AuthInterceptor;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий расходного материала
 */
public class SupplyRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/supplies";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();

    /**
     * Получает список расходных материалов на указанной странице.
     *
     * @param pageIndex индекс страницы для получения (начинается с 0).
     * @return список объектов Supply на указанной странице. Если pageIndex < 0, возвращается пустой список.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<Supply> get(int pageIndex) throws Exception{
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
        Type type = new TypeToken<List<Supply>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает список расходных материалов на указанной странице с фильтрацией.
     *
     * @param pageIndex индекс страницы
     * @param filterName название
     * @param filterCategory категория
     * @param filterOperator оператор сравнения количества
     * @param filterCount общее количество
     * @return список объектов Supply, соответствующих параметрам поиска на указанной странице. Если pageIndex < 0, возвращается пустой список.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<Supply> get(int pageIndex, String filterName, String filterCategory, String filterOperator, Integer filterCount) throws Exception {
        if(pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = "?pageIndex=" + pageIndex;
        if(filterName != null && !filterName.isBlank()){
            filterName = URLEncoder.encode(filterName, StandardCharsets.UTF_8);
            partUrl+="&name=" + filterName;
        }
        if(filterCategory != null && !filterCategory.isBlank()){
            filterCategory = URLEncoder.encode(filterCategory, StandardCharsets.UTF_8);
            partUrl+="&category=" + filterCategory;
        }
        if(filterCount != null && filterOperator != null && !filterOperator.isBlank()){
            filterOperator = URLEncoder.encode(filterOperator, StandardCharsets.UTF_8);
            partUrl+=String.format("&operator=%s&count=%d",filterOperator,filterCount);
        }

        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Supply>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает изображение расходного материала по имени файла фотографии.
     *
     * @param supPhotoName имя файла фотографии расходного материала.
     * @return объект Image, представляющий фотографию расходного материала.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public Image getPhoto(String supPhotoName) throws Exception {
        Request request = new Request.Builder()
                .url(url+"/getPhoto/" + supPhotoName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        InputStream inputStream = response.body().byteStream();
        return new Image(inputStream);
    }

    /**
     * Создает новую расходный материал с возможностью загрузки фотографии.
     *
     * @param supply объект Supply, представляющий новый расходный материал.
     * @param photo файл фотографии для загрузки (может быть null).
     * @return объект Supply, представляющий созданный расходный материал.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public Supply create(Supply supply, File photo) throws Exception{
        Supply createdSupply = null;
        String jsonData = gson.toJson(supply);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("supply", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

        if (photo != null) {
            builder.addFormDataPart("photo", photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")));
        }

        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Supply>() {}.getType();
            createdSupply = gson.fromJson(result, type);
        } else if(response.code() == 400 || response.code() == 409){
            throw new HttpRetryException(response.body().string(), response.code());
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdSupply;
    }

    /**
     * Удаляет расходный материал по указанному id.
     *
     * @param id id расходного материала для удаления.
     * @return true, если удаление прошло успешно; false в противном случае.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
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

    /**
     * Редактирует существующий расходный материал с возможностью загрузки новой фотографии.
     *
     * @param supply объект Supply, представляющий редактируемый расходный материал.
     * @param photo файл новой фотографии для загрузки (может быть null).
     * @return объект Supply, представляющий отредактированный расходный материал.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public Supply edit(Supply supply, File photo) throws Exception{
        Supply editedSupply = null;
        String jsonData = gson.toJson(supply);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("supply", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

        if (photo != null) {
            builder.addFormDataPart("photo", photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")));
        }

        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url + "/edit/"+supply.getSupId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Supply>() {}.getType();
            editedSupply = gson.fromJson(result, type);
        } else if(response.code() == 400 || response.code() == 409){
            throw new HttpRetryException(response.body().string(), response.code());
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedSupply;
    }
}
