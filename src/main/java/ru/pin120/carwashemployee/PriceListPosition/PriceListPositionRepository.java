package ru.pin120.carwashemployee.PriceListPosition;

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
 * Репозиторий позиции прайс-листа
 */
public class PriceListPositionRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/priceList";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();

    /**
     * Получает список позиций прайс-листа по названию услуги.
     *
     * @param servName название услуги для фильтрации позиций прайс-листа.
     * @return список позиций прайс-листа, связанных с указанной услугой.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<PriceListPosition> getByServName(String servName) throws Exception {
        Request request = new Request.Builder()
                .url(url+"/" + servName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<PriceListPosition>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает список услуг с прайс-листом для указанной категории транспорта.
     *
     * @param catTrId id категории транспорта.
     * @return список услуг с прайс-листом, связанных с указанной категорией транспорта.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<ServiceWithPriceList> getCategoryOfTransportPriceList(Long catTrId) throws Exception {
        Request request = new Request.Builder()
                .url(url+"/getPriceListOfCategoryTransport?catTrId=" + catTrId)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<ServiceWithPriceList>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }


    /**
     * Создает новую позицию в прайс-листе
     *
     * @param priceListPosition объект позиции прайс-листа для создания.
     * @return созданная позиция прайс-листа.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public PriceListPosition createPriceListPosition(PriceListPosition priceListPosition) throws Exception {
        PriceListPosition createdPriceListPosition = null;
        String jsonData = gson.toJson(priceListPosition);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<PriceListPosition>(){}.getType();
                createdPriceListPosition = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(response.body().string(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdPriceListPosition;
    }

    /**
     * Редактирует позицию в прайс-листе.
     *
     * @param priceListPosition объект позиции прайс-листа с новыми данными для редактирования.
     * @return отредактированная позиция прайс-листа.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public PriceListPosition editPriceListPositionPriceAndTime(PriceListPosition priceListPosition) throws Exception {
        PriceListPosition editedPriceListPosition = null;
        String jsonData = gson.toJson(priceListPosition);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/priceAndTime/"+priceListPosition.getPlId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<PriceListPosition>(){}.getType();
                editedPriceListPosition = gson.fromJson(result, type);
                break;
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedPriceListPosition;
    }

    /**
     * Удаляет позицию в прайс-листе по её id.
     *
     * @param plId id позиции прайс-листа для удаления.
     * @return {@code true}, если удаление прошло успешно; {@code false} в противном случае.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public boolean deletePriceListPosition(Long plId) throws Exception {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/" + plId)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()) {
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
     * Поиск позиций прайс-листа по различным критериям
     *
     * @param servName название услуги
     * @param catTrName название категории транспорта
     * @param priceOperator оператор сравнения для стоимости
     * @param price стоимость выполнения
     * @param timeOperator оператор сравнения для времени
     * @param time время выполнения
     * @return список позиций прайс-листа, соответствующих критериям поиска.
     * @throws Exception если происходит ошибка при выполнении HTTP-запроса или обработке данных.
     */
    public List<PriceListPosition> searchPriceListPosition(String servName, String catTrName, String priceOperator, Integer price, String timeOperator, Integer time) throws Exception {
        String parameter = "?servName=" + servName;
        if(catTrName != null && !catTrName.isBlank()){
            catTrName = URLEncoder.encode(catTrName, "UTF-8");
            parameter += "&catTrName=" + catTrName;
        }
        if(priceOperator != null && !priceOperator.isBlank()){
            priceOperator = URLEncoder.encode(priceOperator, "UTF-8");
//            if(parameter.contains("?")){
                parameter += String.format("&priceOperator=%s&price=%d", priceOperator, price);
//            }else{
//                parameter += String.format("?priceOperator=%s&price=%d", priceOperator, price);
//            }
        }
        if(timeOperator != null && !timeOperator.isBlank()){
            timeOperator = URLEncoder.encode(timeOperator, "UTF-8");
            //if(parameter.contains("?")){
                parameter += String.format("&timeOperator=%s&time=%d", timeOperator, time);
            //}else{
                //parameter += String.format("?timeOperator=%s&time=%d", timeOperator, time);
            //}
        }

        Request request = new Request.Builder()
                .url(url+parameter)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<PriceListPosition>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }
}
