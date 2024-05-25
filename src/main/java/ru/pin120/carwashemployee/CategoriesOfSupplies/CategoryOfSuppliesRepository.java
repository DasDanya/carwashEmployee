package ru.pin120.carwashemployee.CategoriesOfSupplies;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.List;

public class CategoryOfSuppliesRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/categoriesOfSupplies";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    public List<CategoryOfSupplies> getAll() throws Exception{
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfSupplies>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public List<CategoryOfSupplies> search(String csupName) throws Exception{
        Request request = new Request.Builder()
                .url(url + "?csupName="+csupName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfSupplies>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public CategoryOfSupplies create(CategoryOfSupplies categoryOfSupplies) throws Exception {
        CategoryOfSupplies createdCategory = null;
        String jsonData = gson.toJson(categoryOfSupplies);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                String result = response.body().string();
                Type type = new TypeToken<CategoryOfSupplies>(){}.getType();
                createdCategory = gson.fromJson(result, type);
                break;
            case 409:
                throw new HttpRetryException(categoryOfSupplies.getCsupName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdCategory;
    }

    public boolean delete(String cSupName) throws Exception {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/"+cSupName)
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
