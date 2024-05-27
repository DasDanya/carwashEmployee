package ru.pin120.carwashemployee.WorkSchedule;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.Adapters.LocalDateAdapter;
import ru.pin120.carwashemployee.Adapters.LocalTimeAdapter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Cleaners.CleanerDTO;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class WorkScheduleRepository {
    private static final String url = AppHelper.getCarWashAPI() + "/workSchedule";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalTime.class, new LocalTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

    public List<WorkScheduleDTO> get(LocalDate startInterval, LocalDate endInterval, Long clrId, Integer pageIndex) throws Exception{
        String partUrl = "?startInterval="+startInterval;
        partUrl+="&endInterval="+endInterval;
        partUrl+="&clrId="+clrId;
        if(pageIndex != null) {
            partUrl += "&pageIndex=" + pageIndex;
        }

        Request request = new Request.Builder()
                .url(url + partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<WorkScheduleDTO>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public ResultCreateWorkSchedulesDTO create(List<CleanerDTO> cleanerDTOList) throws Exception{
        ResultCreateWorkSchedulesDTO resultCreate = null;
        String jsonData = gson.toJson(cleanerDTOList);

        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<ResultCreateWorkSchedulesDTO>() {}.getType();
            resultCreate = gson.fromJson(result, type);
        } else if (response.code() == 409) {
            String result = response.body().string();
            Type type = new TypeToken<ResultCreateWorkSchedulesDTO>() {}.getType();
            resultCreate = gson.fromJson(result, type);

            throw new HttpRetryException(resultCreate.getConflictMessage(), response.code());
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return  resultCreate;
    }

    public boolean delete(List<WorkSchedule> deleted) throws Exception {
        boolean successDelete;
        String jsonData = gson.toJson(deleted);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/delete")
                .delete(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 204) {
            successDelete = true;
        } else {
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }

        return successDelete;
    }

}
