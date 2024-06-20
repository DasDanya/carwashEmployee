package ru.pin120.carwashemployee.Cleaners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.scene.image.Image;
import lombok.NonNull;
import okhttp3.*;
import ru.pin120.carwashemployee.Adapters.LocalDateAdapter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Boxes.BoxStatus;
import ru.pin120.carwashemployee.Clients.Client;
import ru.pin120.carwashemployee.FX.FXHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;
import ru.pin120.carwashemployee.Transport.Transport;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.time.LocalDate;
import java.util.Base64;
import java.util.List;

/**
 * Репозиторий мойщика
 */
public class CleanersRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/cleaners";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();


    /**
     * Получает список объектов CleanerDTO с графиком работы за указанный интервал дат.
     * @param startInterval начальная дата интервала
     * @param endInterval конечная дата интервала
     * @param boxId id бокса (параметр запроса)
     * @param currentMonth флаг, указывающий текущий ли месяц (параметр запроса)
     * @return список объектов CleanerDTO, соответствующих критериям запроса
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
    public List<CleanerDTO> getWithWorkSchedule( @NonNull LocalDate startInterval,  @NonNull LocalDate endInterval, @NonNull Long boxId, boolean currentMonth) throws Exception{
        String partUrl = "?startInterval=" + startInterval;
        partUrl+="&endInterval="+endInterval;
        partUrl+="&boxId="+boxId;
        partUrl+="&currentMonth="+currentMonth;
        Request request = new Request.Builder()
                .url(url+ "/workSchedule" + partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<CleanerDTO>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает список объектов Cleaner в соответствии с указанными параметрами фильтрации.
     * @param surname фамилия
     * @param name имя
     * @param patronymic отчество
     * @param phone телефон
     * @param status статус
     * @return список объектов Cleaner, соответствующих критериям запроса
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
    public List<Cleaner> get(String surname, String name, String patronymic, String phone, CleanerStatus status) throws Exception{
        String partUrl = "";
        if(surname != null && !surname.isBlank()){
            surname = URLEncoder.encode(surname, "UTF-8");
            partUrl="?surname=" + surname;
        }
        if(name != null && !name.isBlank()){
            name = URLEncoder.encode(name, "UTF-8");
            if(partUrl.contains("?")) {
                partUrl += "&name=" + name;
            }else{
                partUrl += "?name=" + name;
            }
        }
        if(patronymic != null && !patronymic.isBlank()){
            patronymic = URLEncoder.encode(patronymic, "UTF-8");
            if(partUrl.contains("?")) {
                partUrl += "&patronymic=" + patronymic;
            }else{
                partUrl += "?patronymic=" + patronymic;
            }
        }
        if(phone != null && !phone.isBlank()){
            phone = URLEncoder.encode(phone, "UTF-8");
            if(partUrl.contains("?")) {
                partUrl += "&phone=" + phone;
            }else{
                partUrl += "?phone=" + phone;
            }
        }
        if(status != null){
            //String status = URLEncoder.encode(cleanerStatus.name(), "UTF-8");
            if(partUrl.contains("?")) {
                partUrl += "&status=" + status;
            }else{
                partUrl += "?status=" + status;
            }
        }

        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Cleaner>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * Получает изображение фотографии мойщика по названию файла.
     * @param photoName название файла фотографии
     * @return объект Image, представляющий изображение фотографии
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
    public Image getPhoto(String photoName) throws Exception{
        Request request = new Request.Builder()
                .url(url+"/getPhoto/" + photoName)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
//            String photoBase64 = response.body().string();
//            byte[] decodedBytes = Base64.getDecoder().decode(photoBase64);
        InputStream inputStream = response.body().byteStream();

        return new Image(inputStream);
    }

    /**
     * Создает нового мойщика с указанными данными и фотографией.
     * @param cleaner объект Cleaner, содержащий данные нового мойщика
     * @param photo файл фотографии нового мойщика (может быть null)
     * @return созданный объект Cleaner
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
    public Cleaner create(Cleaner cleaner, File photo) throws Exception{
        Cleaner createdCleaner = null;
        String jsonData = gson.toJson(cleaner);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cleaner", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

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
            Type type = new TypeToken<Cleaner>() {}.getType();
            createdCleaner = gson.fromJson(result, type);
        } else if(response.code() == 400){
            throw new HttpRetryException(response.body().string(), 400);
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return createdCleaner;
    }

    /**
     * Удаляет мойщика по его id.
     * @param id id мойщика для удаления
     * @return true, если удаление успешно выполнено; false в противном случае
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
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

    /**
     * Изменяет данные о существующем мойщике с указанными данными и фотографией.
     * @param cleaner объект Cleaner, содержащий обновленные данные мойщика
     * @param photo файл фотографии мойщика для обновления (может быть null)
     * @return обновленный объект Cleaner
     * @throws Exception если возникает ошибка при выполнении HTTP запроса или обработке ответа
     */
    public Cleaner edit(Cleaner cleaner, File photo) throws Exception{
        Cleaner editedCleaner = null;
        String jsonData = gson.toJson(cleaner);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("cleaner", null, RequestBody.create(jsonData, MediaType.parse("application/json; charset=utf-8")));

        if (photo != null) {
            builder.addFormDataPart("photo", photo.getName(), RequestBody.create(photo, MediaType.parse("image/*")));
        }

        MultipartBody body = builder.build();

        Request request = new Request.Builder()
                .url(url + "/edit/"+cleaner.getClrId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Cleaner>() {}.getType();
            editedCleaner = gson.fromJson(result, type);
        } else if(response.code() == 400){
            throw new HttpRetryException(response.body().string(), 400);
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return editedCleaner;
    }
}
