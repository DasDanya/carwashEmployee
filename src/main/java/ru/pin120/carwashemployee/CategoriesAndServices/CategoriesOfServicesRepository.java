package ru.pin120.carwashemployee.CategoriesAndServices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CategoriesOfServicesRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/categoryOfServices";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();
//    public List<CategoriesWithServicesDTO> getCategoriesWithServices() throws Exception {
//        Request request = new Request.Builder()
//                .url(url + "/getCategoriesWithServices")
//                .build();
//
//        Response response = client.newCall(request).execute();
//        if(response.code() != 200){
//            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
//        }
//        String jsonData = response.body().string();
//        Type type = new TypeToken<List<CategoriesWithServicesDTO>>(){}.getType();
//
//        return gson.fromJson(jsonData, type);
//    }

    public List<CategoryOfServices> getCategoriesWithServices() throws Exception {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CategoryOfServices>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public List<String> getCategoriesName() throws Exception{
        Request request = new Request.Builder()
                .url(url + "/getAllCatNames")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<String>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public List<String> getCategoriesNameByParameter(String parameter) throws Exception {
        Request request = new Request.Builder()
                .url(url + "/getCatNamesByParameter/"+parameter)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<String>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public boolean createCategoryOfServices(CategoryOfServices categoryOfServices) throws Exception{
        boolean successCreate;
        String jsonData = gson.toJson(categoryOfServices);
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
                throw new HttpRetryException(categoryOfServices.getCatName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successCreate;
    }

    public boolean editCategoryOfServices(EditCategoryOrServiceDTO editCategoryOrServiceDTO) throws Exception{
        boolean successEdit;
        String jsonData = gson.toJson(editCategoryOrServiceDTO);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit")
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                successEdit = true;
                break;
            case 409:
                throw new HttpRetryException(editCategoryOrServiceDTO.getNewName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successEdit;
    }

    public boolean deleteCategoryOfServices(CategoryOfServices categoryOfServices) throws Exception {
        boolean successDelete;
        //String jsonData = gson.toJson(categoryOfServices);
        //System.out.println(jsonData);
        //RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/delete/"+categoryOfServices.getCatName())
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
