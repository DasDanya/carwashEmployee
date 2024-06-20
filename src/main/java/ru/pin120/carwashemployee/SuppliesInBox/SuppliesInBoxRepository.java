package ru.pin120.carwashemployee.SuppliesInBox;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.Supplies.Supply;
import ru.pin120.carwashemployee.Transport.Transport;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Репозиторий расходных материалов в боксе
 */
public class SuppliesInBoxRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/suppliesInBox";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();

    public SuppliesInBox add(SuppliesInBox suppliesInBox) throws Exception{
        SuppliesInBox createdSuppliesInBox = null;
        String jsonData = gson.toJson(suppliesInBox);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/add")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<SuppliesInBox>() {}.getType();
            createdSuppliesInBox = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return createdSuppliesInBox;
    }

    public SuppliesInBox transferToWarehouse(AddSuppliesFromBoxDTO addSuppliesFromBoxDTO) throws Exception{
        SuppliesInBox editedSuppliesInBox = null;
        String jsonData = gson.toJson(addSuppliesFromBoxDTO);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/transferToWarehouse/"+addSuppliesFromBoxDTO.getSuppliesInBox().getSibId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<SuppliesInBox>() {}.getType();
            editedSuppliesInBox = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return editedSuppliesInBox;
    }

    public SuppliesInBox edit(SuppliesInBox suppliesInBox) throws Exception{
        SuppliesInBox editedSuppliesInBox = null;
        String jsonData = gson.toJson(suppliesInBox);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+suppliesInBox.getSibId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<SuppliesInBox>() {}.getType();
            editedSuppliesInBox = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return editedSuppliesInBox;
    }

    public List<SuppliesInBox> get(Long boxId, int pageIndex, String filterName, String filterCategory, String filterOperator, Integer filterCount) throws Exception {
        if(pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = String.format("?pageIndex=%d&boxId=%d",pageIndex,boxId);
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
                .url(url+"/byBox"+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<SuppliesInBox>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public boolean delete(Long id) throws Exception {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/"+id)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 204) {
            successDelete = true;
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return successDelete;
    }
}
