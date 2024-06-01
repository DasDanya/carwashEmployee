package ru.pin120.carwashemployee.Bookings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.Adapters.LocalDateTimeAdapter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Cleaners.CleanerDTO;
import ru.pin120.carwashemployee.ClientsTransport.ClientsTransport;

import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.time.LocalDateTime;
import java.util.List;

public class BookingsRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/bookings";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    public List<Booking> getBoxBookings(LocalDateTime startInterval, LocalDateTime endInterval, Long boxId) throws Exception{
        String partUrl = "?startInterval=" + startInterval;
        partUrl+="&endInterval="+endInterval;
        partUrl+="&boxId="+boxId;

        Request request = new Request.Builder()
                .url(url+ "/boxBookings" + partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Booking>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    public Booking create(BookingDTO transport) throws Exception {
        Booking createdBooking = null;
        String jsonData = gson.toJson(transport);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/create")
                .post(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Booking>() {}.getType();
            createdBooking = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), 409);
        }

        return createdBooking;
    }

}
