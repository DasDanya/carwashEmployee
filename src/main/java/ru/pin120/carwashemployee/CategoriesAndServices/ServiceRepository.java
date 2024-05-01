package ru.pin120.carwashemployee.CategoriesAndServices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.util.List;

public class ServiceRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/services";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new Gson();


    public List<Service> getServicesByCatName(String catName) throws Exception{
        Request request = new Request.Builder()
                .url(url + "?categoryName=" + catName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Service>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public boolean createService(ServiceDTO serviceDTO) throws Exception {
        boolean successCreate;
        String jsonData = gson.toJson(serviceDTO);

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
                throw new HttpRetryException(serviceDTO.getServName() + " " + AppHelper.getExistsEntityTextEnd(), 409);
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successCreate;
    }

    public boolean bindServicesToCategory(String pastCategoryName, String newCategoryName) throws Exception{
        boolean successCreateBind = false;
        BindWithCategoryDTO bindWithCategoryDTO = new BindWithCategoryDTO(pastCategoryName, newCategoryName);

        String jsonData = gson.toJson(bindWithCategoryDTO);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/bindServicesToCategory")
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                successCreateBind = true;
                break;
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successCreateBind;
    }

    public boolean bindServiceToCategory(String servName, String newCategoryName) throws Exception{
        boolean successCreateBind = false;
        BindWithCategoryDTO bindWithCategoryDTO = new BindWithCategoryDTO(servName, newCategoryName);

        String jsonData = gson.toJson(bindWithCategoryDTO);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/bindServiceToCategory")
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        switch (response.code()){
            case 200:
                successCreateBind = true;
                break;
            default:
                throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successCreateBind;
    }

    public boolean deleteService(ServiceDTO serviceDTO) throws Exception {
        boolean successDelete;
        String jsonData = gson.toJson(serviceDTO);
        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/delete")
                .delete(body)
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
