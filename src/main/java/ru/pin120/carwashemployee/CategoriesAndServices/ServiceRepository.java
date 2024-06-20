package ru.pin120.carwashemployee.CategoriesAndServices;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.CategoriesAndServices.BindWithCategoryDTO;
import ru.pin120.carwashemployee.CategoriesAndServices.Service;
import ru.pin120.carwashemployee.CategoriesAndServices.ServiceDTO;
import ru.pin120.carwashemployee.CategoriesOfTransport.CategoryOfTransport;
import ru.pin120.carwashemployee.Http.AuthInterceptor;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Репозиторий услуги
 */
public class ServiceRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/services";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new Gson();


    /**
     * Получает список услуг по навзанию категории.
     *
     * @param catName название категории
     * @return список услуг, принадлежащих данной категории
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
    public List<Service> getServicesByCatName(String catName) throws Exception{
        //catName = URLEncoder.encode(catName, "UTF-8");
        Request request = new Request.Builder()
                .url(url + "/" + catName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Service>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает услугу по ее названию.
     *
     * @param servName название услуги
     * @return объект Service
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
    public Service get(String servName) throws Exception{
        Request request = new Request.Builder()
                .url(url + "/get/" +  servName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<Service>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public List<ServiceDTO> getAllServices() throws Exception{
        Request request = new Request.Builder()
                .url(url + "/all")
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<ServiceDTO>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает объект ServiceDTO по названию услуги.
     *
     * @param servName название услуги
     * @return объект ServiceDTO
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
    public ServiceDTO getServiceDTOByServName(String servName) throws Exception{
        //servName = URLEncoder.encode(servName, "UTF-8");
        Request request = new Request.Builder()
                .url(url + "/getByServName/" + servName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<ServiceDTO>(){}.getType();

        return gson.fromJson(jsonData, type);
    }


    /**
     * Создает новую услугу.
     *
     * @param serviceDTO объект ServiceDTO, представляющий новую услугу
     * @return true, если услуга успешно создана, иначе false
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
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

    /**
     * Привязывает все услуги из одной категории к другой категории
     *
     * @param pastCategoryName название текущей категории
     * @param newCategoryName название новой категории
     * @return true, если привязка успешно выполнена, иначе false
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
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


    /**
     * Привязывает услугу к новой категории.
     *
     * @param servName название услуги
     * @param newCategoryName название новой категории
     * @return true, если привязка успешно выполнена, иначе false
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
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

    /**
     * Удаляет услугу.
     *
     * @param serviceDTO объект ServiceDTO, представляющий услугу для удаления
     * @return true, если услуга успешно удалена, иначе false
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
    public boolean deleteService(ServiceDTO serviceDTO) throws Exception {
        boolean successDelete;
        //String jsonData = gson.toJson(serviceDTO);
        //System.out.println(jsonData);
        //RequestBody body = RequestBody.create(JSON, jsonData);

        //String servName = URLEncoder.encode(serviceDTO.getServName(), "UTF-8");
        Request request = new Request.Builder()
                .url(url + "/delete/"+serviceDTO.getServName())
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
     * Редактирует привязку категорий расходных материалов к услуге
     *
     * @param service объект Service, представляющий услугу для редактирования
     * @return объект Service с обновленными данными
     * @throws Exception если возникает ошибка при выполнении HTTP-запроса
     */
    public Service editCategoriesOfSupplies(Service service) throws Exception{
        Service editedService = null;
        String jsonData = gson.toJson(service);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/necessaryCategoriesOfSupplies/"+service.getServName())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Service>() {}.getType();
            editedService = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
            //throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedService;
    }
}
