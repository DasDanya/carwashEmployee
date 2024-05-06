package ru.pin120.carwashemployee.CategoriesOfTransport;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.List;

public class CategoryOfTransportRepository {


    private static final String url = AppHelper.getCarWashAPI() + "/categoryOfTransport";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();

    private Gson gson = new Gson();


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

    public List<CategoryOfTransport> getCategoriesOfTransportWithoutPriceAndTime(String servName) throws Exception {
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


    public boolean deleteCategoryOfTransport(Long id) throws IOException {
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
