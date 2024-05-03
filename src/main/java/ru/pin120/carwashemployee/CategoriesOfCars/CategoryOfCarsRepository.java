package ru.pin120.carwashemployee.CategoriesOfCars;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.List;

public class CategoryOfCarsRepository  {


    private static final String url = AppHelper.getCarWashAPI() + "/categoryOfCars";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private Gson gson = new Gson();


    public List<CategoryOfCars> getAll() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfCars>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public boolean createCategoryOfCars(CategoryOfCars categoryOfCars) throws Exception {
        boolean successCreate;
        String jsonData = gson.toJson(categoryOfCars);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                successCreate = true;
                break;
            case 409:
                throw new HttpRetryException(categoryOfCars.getCatCarsName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successCreate;
    }

    public boolean deleteCategoryOfCars(String catCarsName) throws IOException {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/"+catCarsName)
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

    public List<CategoryOfCars> getCategoriesOfCarsByCarCarsName(String parameter) throws Exception {
        Request request = new Request.Builder()
                .url(url + "?catCarsName=" + parameter)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfCars>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }
}
