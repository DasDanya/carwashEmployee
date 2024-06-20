package ru.pin120.carwashemployee.ClientsTransport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.List;

/**
 * Репозиторий транспорта клиента
 */
public class ClientsTransportRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/clientsTransport";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();

    /**
     * Получает список транспортных средств клиента по id клиента.
     *
     * @param clientId id клиента для поиска транспортных средств.
     * @return Список объектов ClientsTransport
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<ClientsTransport> getByClientId(Long clientId) throws Exception{
        Request request = new Request.Builder()
                .url(url+"/byClient?clId=" + clientId)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<ClientsTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает список транспортных средств по государственному номеру
     *
     * @param stateNumber Государственный номер транспортного средства для поиска.
     * @return Список объектов ClientsTransport, соответствующих указанному государственному номеру.
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<ClientsTransport> getByStateNumber(String stateNumber) throws Exception{
        stateNumber = URLEncoder.encode(stateNumber, "UTF-8");
        Request request = new Request.Builder()
                .url(url+"/byStateNumber?stateNumber=" + stateNumber)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<ClientsTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Создает новую запись транспортного средства.
     *
     * @param transport Объект ClientsTransport, содержащий данные о транспортном средстве для создания.
     * @return Созданный объект ClientsTransport
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public ClientsTransport create(ClientsTransport transport) throws Exception {
        ClientsTransport createdTransport = null;
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
                Type type = new TypeToken<ClientsTransport>(){}.getType();
                createdTransport = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(response.body().string(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdTransport;
    }

    /**
     * Изменяет существующую запись транспортного средства.
     *
     * @param transport Объект ClientsTransport с измененными данными для сохранения.
     * @return Обновленный объект ClientsTransport с актуальными данными после изменения.
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public ClientsTransport edit(ClientsTransport transport) throws Exception {
        ClientsTransport editedTransport = null;
        String jsonData = gson.toJson(transport);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+transport.getClTrId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<ClientsTransport>(){}.getType();
                editedTransport = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(response.body().string(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedTransport;
    }

    /**
     * Удаляет запись транспортного средства по его id.
     *
     * @param id id транспортного средства для удаления.
     * @return true, если удаление успешно выполнено; false в противном случае.
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
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
     * Выполняет поиск транспортных средств клиента по заданным параметрам.
     *
     * @param clId id клиента для поиска связанных транспортных средств.
     * @param mark Марка транспортного средства
     * @param model Модель транспортного средства
     * @param category Категория транспортного средства
     * @param stateNumber Государственный номер транспортного средства
     * @return Список объектов ClientsTransport, соответствующих указанным параметрам поиска.
     * @throws Exception Если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<ClientsTransport> searchClientTransport(Long clId, String mark, String model, String category, String stateNumber) throws Exception {
        String partUrl = "/byClient?clId=" + clId;
        if(mark != null && !mark.isBlank()){
            mark = URLEncoder.encode(mark, "UTF-8");
            partUrl+="&mark=" + mark;
        }
        if(model != null && !model.isBlank()){
            model = URLEncoder.encode(model, "UTF-8");
            partUrl+="&model=" + model;
        }
        if(category != null && !category.isBlank()){
            category = URLEncoder.encode(category, "UTF-8");
            partUrl+="&category=" + category;
        }
        if(stateNumber != null && !stateNumber.isBlank()){
            stateNumber = URLEncoder.encode(stateNumber, "UTF-8");
            partUrl+="&stateNumber=" + stateNumber;
        }

        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<ClientsTransport>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

}
