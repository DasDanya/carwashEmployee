package ru.pin120.carwashemployee.Bookings;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import okhttp3.*;
import ru.pin120.carwashemployee.Adapters.LocalDateAdapter;
import ru.pin120.carwashemployee.Adapters.LocalDateTimeAdapter;
import ru.pin120.carwashemployee.AppHelper;
import ru.pin120.carwashemployee.Http.AuthInterceptor;


import java.lang.reflect.Type;
import java.net.HttpRetryException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ����������� ������
 */
public class BookingsRepository {

    private static final String url = AppHelper.getCarWashAPI() + "/bookings";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    private OkHttpClient client = new OkHttpClient.Builder()
            .addInterceptor(new AuthInterceptor())
            .build();
    private Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();


    /**
     * �������� ������ ������� � ������������ ���������� �� ��������� ����������.
     *
     * @param pageIndex      ������ �������� ��� ���������
     * @param cleanerId      ID �������
     * @param clientId       ID �������
     * @param boxId          ID �����
     * @param startInterval  ��������� �������� �������
     * @param endInterval    �������� �������� �������
     * @param bookingStatus  ������ ������
     * @param compareOperator �������� ��������� ��� ����������
     * @param price          ���������
     * @return ������ �������� {@link Booking}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
    public List<Booking> get(int pageIndex,Long cleanerId, Long clientId, Long boxId, LocalDateTime startInterval, LocalDateTime endInterval, String bookingStatus, String compareOperator, Integer price) throws Exception {
        if(pageIndex < 0){
            return new ArrayList<>();
        }

        String partUrl = "?pageIndex=" + pageIndex;
        if(cleanerId != null){
            partUrl+="&cleanerId=" + cleanerId;
        }
        if(clientId != null){
            partUrl+="&clientId=" + clientId;
        }
        if(boxId != null){
            partUrl+="&boxId=" + boxId;
        }
        if(startInterval != null){
            partUrl+="&startInterval=" + startInterval;
        }
        if(endInterval != null){
            partUrl+="&endInterval=" + endInterval;
        }
        if(bookingStatus != null && !bookingStatus.isBlank()){
            bookingStatus = URLEncoder.encode(bookingStatus, StandardCharsets.UTF_8);
            partUrl+="&status=" + bookingStatus;
        }
        if(price != null && compareOperator != null && !compareOperator.isBlank()){
            compareOperator = URLEncoder.encode(compareOperator, StandardCharsets.UTF_8);
            partUrl+=String.format("&operator=%s&price=%d",compareOperator,price);
        }


        Request request = new Request.Builder()
                .url(url+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<List<Booking>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }


    /**
     * �������� ���������� � ������������� � ������������ ���������� �� ��������� ����������
     *
     * @param cleanerId      ID �������
     * @param clientId       ID �������
     * @param boxId          ID �����
     * @param startInterval  ��������� �������� �������
     * @param endInterval    �������� �������� �������
     * @param bookingStatus  �����
     * @param compareOperator �������� ��������� ��� ���������
     * @param price          ���������
     * @return ������ {@link BookingInfoDTO}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
    public BookingInfoDTO getInfo(Long cleanerId, Long clientId, Long boxId, LocalDateTime startInterval, LocalDateTime endInterval, String bookingStatus, String compareOperator, Integer price) throws Exception {
        String partUrl = "";
        if(cleanerId != null){
            partUrl+="?cleanerId=" + cleanerId;
        }
        if(clientId != null){
            if(partUrl.isBlank()){
                partUrl += "?clientId=" + clientId;
            }else {
                partUrl += "&clientId=" + clientId;
            }
        }
        if(boxId != null){
            if(partUrl.isBlank()) {
                partUrl += "?boxId=" + boxId;
            }else{
                partUrl += "&boxId=" + boxId;
            }
        }
        if(startInterval != null){
            if(partUrl.isBlank()) {
                partUrl += "?startInterval=" + startInterval;
            }else{
                partUrl += "&startInterval=" + startInterval;
            }
        }
        if(endInterval != null){
            if(partUrl.isBlank()) {
                partUrl += "?endInterval=" + endInterval;
            }else{
                partUrl += "&endInterval=" + endInterval;
            }
        }
        if(bookingStatus != null && !bookingStatus.isBlank()){
            bookingStatus = URLEncoder.encode(bookingStatus, StandardCharsets.UTF_8);
            if(partUrl.isBlank()) {
                partUrl += "?status=" + bookingStatus;
            }else{
                partUrl += "&status=" + bookingStatus;
            }
        }
        if(price != null && compareOperator != null && !compareOperator.isBlank()){
            compareOperator = URLEncoder.encode(compareOperator, StandardCharsets.UTF_8);
            if(partUrl.isBlank()) {
                partUrl += String.format("?operator=%s&price=%d", compareOperator, price);
            }else{
                partUrl += String.format("&operator=%s&price=%d", compareOperator, price);
            }
        }


        Request request = new Request.Builder()
                .url(url+"/getInfo"+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<BookingInfoDTO>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * �������� ���������� � ������ ������� �� ������������ ������
     *
     * @param cleanerId     ID �������
     * @param startInterval ��������� �������� �������
     * @param endInterval   �������� �������� �������
     * @return Map � ������� - ����� ������ ������� {@link LocalDate} � ���������� - ������� � ������ � ���� ���� {@link BookingInfoDTO}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
    public Map<LocalDate, BookingInfoDTO> getInfoAboutWorkOfCleaner(Long cleanerId, LocalDateTime startInterval, LocalDateTime endInterval) throws Exception{
        String partUrl = "?cleanerId=" + cleanerId;
        if(startInterval != null){
            partUrl += "&startInterval=" + startInterval;
        }
        if(endInterval != null){
            partUrl += "&endInterval=" + endInterval;
        }

        Request request = new Request.Builder()
                .url(url+"/getInfoAboutWorkOfCleaner"+partUrl)
                .build();

        Response response = client.newCall(request).execute();
        if(response.code() != 200){
            throw new HttpRetryException(AppHelper.getHttpErrorText() + " " + response.code(), response.code());
        }
        String jsonData = response.body().string();
        Type type = new TypeToken<Map<LocalDate, BookingInfoDTO>>(){}.getType();

        return gson.fromJson(jsonData, type);
    }

    /**
     * �������� ������ ������� ��� ����������� ����� �� ��������� �������� �������
     *
     * @param startInterval ��������� �������� �������
     * @param endInterval   �������� �������� �������
     * @param boxId         ID �����.
     * @return ������ �������� {@link Booking}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
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

    /**
     * ������� ����� �����
     *
     * @param bookingDTO ������ {@link BookingDTO}, ���������� ������ ������ ������
     * @return ��������� ������ {@link Booking}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
    public Booking create(BookingDTO bookingDTO) throws Exception {
        Booking createdBooking = null;
        String jsonData = gson.toJson(bookingDTO);

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
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return createdBooking;
    }

    /**
     * ������������� ����� ������ ��� ������
     *
     * @param bookingDTO ������ {@link BookingDTO}, ���������� ������ � ������ � ����� ��������.
     * @return ����������� ������ {@link Booking}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������
     */
    public Booking setNewStatus(BookingDTO bookingDTO) throws Exception {
        Booking editedBooking = null;
        String jsonData = gson.toJson(bookingDTO);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/newStatus/"+bookingDTO.getBkId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Booking>() {}.getType();
            editedBooking = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return editedBooking;
    }

    /**
     * ����������� ������������ �����
     *
     * @param bookingDTO ������ {@link BookingDTO}, ���������� ����������� ������ ������
     * @return ����������� ������ {@link Booking}.
     * @throws Exception ���� ��������� ������ ��� ���������� �������.
     */
    public Booking edit(BookingDTO bookingDTO) throws Exception {
        Booking editedBooking = null;
        String jsonData = gson.toJson(bookingDTO);

        System.out.println(jsonData);
        RequestBody body = RequestBody.create(JSON, jsonData);

        Request request = new Request.Builder()
                .url(url + "/edit/"+bookingDTO.getBkId())
                .put(body)
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 200) {
            String result = response.body().string();
            Type type = new TypeToken<Booking>() {}.getType();
            editedBooking = gson.fromJson(result, type);
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return editedBooking;
    }

    /**
     * ������� ����� �� ��� ID
     *
     * @param id ID ������
     * @return true, ���� ����� ������� ������, ����� false
     * @throws Exception ���� ��������� ������ ��� ���������� �������.
     */
    public boolean delete(String id) throws Exception {
        boolean successDelete;

        Request request = new Request.Builder()
                .url(url + "/delete/"+id)
                .delete()
                .build();

        Response response = client.newCall(request).execute();
        if (response.code() == 204) {
            successDelete = true;
        } else {
            throw new HttpRetryException(response.body().string(), response.code());
        }

        return successDelete;
    }

}
