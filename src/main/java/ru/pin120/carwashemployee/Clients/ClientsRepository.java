package ru.pin120.carwashemployee.Clients;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.Transport.Transport;


import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий клиента
 */
public class ClientsRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/clients";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();


    /**
     * Получает список клиентов указанной страницы.
     * Если указанный индекс страницы меньше нуля, возвращает пустой список.
     *
     * @param pageIndex индекс страницы
     * @return список клиентов указанной страницы
     * @throws Exception если произошла ошибка при выполнении HTTP-запроса или обработке данных
     */
    public List<Client> getByPage(int pageIndex) throws Exception {
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
        Type type = new TypeToken<List<Client>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Выполняет поиск клиентов с учетом заданных фильтров и страницы.
     * Если указанный индекс страницы меньше нуля, возвращает пустой список.
     *
     * @param pageIndex индекс страницы
     * @param surname фамилия клиента
     * @param name имя клиента
     * @param phone номер телефона клиента
     * @param discount скидка клиента
     * @param filterDiscountOperator оператор сравнения для фильтрации скидки
     * @return список клиентов, соответствующих заданным фильтрам и находящихся на указанной странице
     * @throws Exception если произошла ошибка при выполнении HTTP-запроса или обработке данных
     */
    public List<Client> search(int pageIndex, String surname, String name, String phone, Integer discount, String filterDiscountOperator) throws Exception {
        if (pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = "?pageIndex=" + pageIndex;
        if(surname != null && !surname.isBlank()){
            surname = URLEncoder.encode(surname, "UTF-8");
            partUrl+="&surname=" + surname;
        }
        if(name != null && !name.isBlank()){
            name = URLEncoder.encode(name, "UTF-8");
            partUrl+="&name=" + name;
        }
        if(phone != null && !phone.isBlank()){
            phone = URLEncoder.encode(phone, "UTF-8");
            partUrl+="&phone=" + phone;
        }
        if(discount != null && filterDiscountOperator != null && !filterDiscountOperator.isBlank()){
            filterDiscountOperator = URLEncoder.encode(filterDiscountOperator, "UTF-8");
            partUrl+=String.format("&discount=%d&filterDiscountOperator=%s",discount,filterDiscountOperator);
        }

        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Client>>(){}.getType();

        return gson.fromJson(jsonData, type);

    }

    /**
     * Создает нового клиента
     *
     * @param clientData данные нового клиента
     * @return созданный клиент
     * @throws Exception если произошла ошибка при выполнении HTTP-запроса или обработке данных
     */
    public Client create(Client clientData) throws Exception {
        Client createdClient = null;
        String jsonData = gson.toJson(clientData);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Client>() {}.getType();
            createdClient = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdClient;
    }


    /**
     * Редактирует существующего клиента
     *
     * @param clientData данные клиента для редактирования
     * @return отредактированный клиент
     * @throws Exception если произошла ошибка при выполнении HTTP-запроса или обработке данных
     */
    public Client edit(Client clientData) throws Exception {
        Client editedClient = null;
        String jsonData = gson.toJson(clientData);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+clientData.getClId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Client>() {}.getType();
            editedClient = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedClient;
    }

    /**
     * Удаляет клиента с указанным id.
     *
     * @param id id клиента для удаления
     * @return true, если клиент успешно удален; false в противном случае
     * @throws Exception если произошла ошибка при выполнении HTTP-запроса или обработке данных
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
