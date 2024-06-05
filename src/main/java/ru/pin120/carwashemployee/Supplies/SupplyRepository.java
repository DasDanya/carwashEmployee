package ru.pin120.carwashemployee.Supplies;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Cleaners.Cleaner;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.Http.AuthInterceptor;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class SupplyRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/supplies";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();

    public List<Supply> get(int pageIndex) throws Exception{
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
        Type type = new TypeToken<List<Supply>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public List<Supply> get(int pageIndex, String filterName, String filterCategory, String filterOperator, Integer filterCount) throws Exception {
        if(pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = "?pageIndex=" + pageIndex;
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
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Supply>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public Image getPhoto(String supPhotoName) throws Exception {
        Request request = new Request.Builder()
                .url(url+"/getPhoto/" + supPhotoName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        InputStream inputStream = response.body().byteStream();
        return new Image(inputStream);
    }

    public Supply create(Supply supply, File photo) throws Exception{
        Supply createdSupply = null;
        String jsonData = gson.toJson(supply);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("supply", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

        if (photo != null) {
            builder.addFormDataPart("photo", photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")));
        }

        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Supply>() {}.getType();
            createdSupply = gson.fromJson(result, type);
        } else if(response.code() == 400 || response.code() == 409){
            throw new HttpRetryException(response.body().string(), response.code());
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdSupply;
    }

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

    public Supply edit(Supply supply, File photo) throws Exception{
        Supply editedSupply = null;
        String jsonData = gson.toJson(supply);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("supply", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

        if (photo != null) {
            builder.addFormDataPart("photo", photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")));
        }

        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url + "/edit/"+supply.getSupId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Supply>() {}.getType();
            editedSupply = gson.fromJson(result, type);
        } else if(response.code() == 400 || response.code() == 409){
            throw new HttpRetryException(response.body().string(), response.code());
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedSupply;
    }
}
