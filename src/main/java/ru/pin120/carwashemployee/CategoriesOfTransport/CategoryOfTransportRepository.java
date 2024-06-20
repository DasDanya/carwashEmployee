package ru.pin120.carwashemployee.CategoriesOfTransport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Репозиторий категории транспорта
 */
public class CategoryOfTransportRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/categoriesOfTransport";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();

    private Gson gson = new Gson();

    /**
     * Получает все категории транспорта
     *
     * @return Список объектов {@code CategoryOfTransport}, содержащий данные о категориях транспорта.
     * @throws Exception Если происходит ошибка HTTP или проблемы с парсингом JSON.
     */
    public List<CategoryOfTransport> getAll() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает категории транспорта, для которых не установлена стоимость и время выполнения определенной услуги
     *
     * @param servName Название услуги, для которой необходимо получить категории транспорта.
     * @return Список объектов {@code CategoryOfTransport}, удовлетворяющих условиям запроса.
     * @throws Exception Если происходит ошибка HTTP или проблемы с парсингом JSON.
     */
    public List<CategoryOfTransport> getCategoriesOfTransportWithoutPriceAndTime(String servName) throws Exception {
        //servName = URLEncoder.encode(servName, "UTF-8");
        Request request = new Request.Builder()
                .url(url+"/emptyCategoryTransport/" + servName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает доступные категории транспорта для указанного транспорта
     *
     * @param transportMark Марка транспорта.
     * @param transportModel Модель транспорта.
     * @param trId Идентификатор транспорта (может быть {@code null}).
     * @return Список объектов {@code CategoryOfTransport}, доступных для указанного транспорта.
     * @throws Exception Если происходит ошибка HTTP или проблемы с парсингом JSON.
     */
    public List<CategoryOfTransport> getAvailableCategoriesForTransport(String transportMark, String transportModel, Long trId) throws Exception {
        String partOfUrl;
        if(trId == null) {
            partOfUrl = String.format("/availableCategories?mark=%s&model=%s", transportMark, transportModel);
        }else{
            partOfUrl = String.format("/availableCategories?mark=%s&model=%s&trId=%d", transportMark, transportModel,trId);
        }
        Request request = new Request.Builder()
                .url(url+ partOfUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }


    /**
     * Создает новую категорию транспорта
     *
     * @param categoryOfTransport Объект {@code CategoryOfTransport}, который необходимо создать.
     * @return Созданный объект {@code CategoryOfTransport}.
     * @throws Exception Если происходит ошибка HTTP, категория уже существует или проблемы с парсингом JSON.
     */
    public CategoryOfTransport createCategoryOfTransport(CategoryOfTransport categoryOfTransport) throws Exception {
        CategoryOfTransport createdCategory = null;
        String jsonData = gson.toJson(categoryOfTransport);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<CategoryOfTransport>(){}.getType();
                createdCategory = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(categoryOfTransport.getCatTrName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdCategory;
    }

    /**
     * Изменяет данные о существующей категории транспорта
     *
     * @param categoryOfTransport Объект {@code CategoryOfTransport}, данные которого необходимо изменить.
     * @return Измененный объект {@code CategoryOfTransport}.
     * @throws Exception Если происходит ошибка HTTP, категория не существует или проблемы с парсингом JSON.
     */
    public CategoryOfTransport editCategoryOfTransport(CategoryOfTransport categoryOfTransport) throws Exception {
        CategoryOfTransport editedCategory = null;
        String jsonData = gson.toJson(categoryOfTransport);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+categoryOfTransport.getCatTrId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<CategoryOfTransport>(){}.getType();
                editedCategory = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(categoryOfTransport.getCatTrName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedCategory;
    }

    /**
     * Удаляет категорию транспорта с указанным id
     *
     * @param id id категории транспорта для удаления.
     * @return {@code true}, если категория успешно удалена, {@code false} в противном случае.
     * @throws Exception Если происходит ошибка HTTP или проблемы с парсингом JSON.
     */
    public boolean deleteCategoryOfTransport(Long id) throws Exception {
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
     * Получает категории транспорта по указанному названию
     *
     * @param parameter Название категории транспорта для поиска.
     * @return Список объектов {@code CategoryOfTransport}, удовлетворяющих критерию поиска.
     * @throws Exception Если происходит ошибка HTTP или проблемы с парсингом JSON.
     */
    public List<CategoryOfTransport> getCategoriesOfTransportByCatTrName(String parameter) throws Exception {
        Request request = new Request.Builder()
                .url(url + "/" + parameter)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }
}
