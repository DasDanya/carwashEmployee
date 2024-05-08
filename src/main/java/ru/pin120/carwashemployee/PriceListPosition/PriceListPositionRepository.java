package ru.pin120.carwashemployee.PriceListPosition;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.util.List;

public class PriceListPositionRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/priceList";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();

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

    public boolean deletePriceListPosition(Long plId) throws IOException {
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
