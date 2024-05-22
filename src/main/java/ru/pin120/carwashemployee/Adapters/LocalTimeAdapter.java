package ru.pin120.carwashemployee.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class LocalTimeAdapter extends TypeAdapter<LocalTime> {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    @Override
    public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
        jsonWriter.value(localTime != null ? localTime.format(formatter) : null);
    }

    @Override
    public LocalTime read(JsonReader jsonReader) throws IOException {
        String time = jsonReader.nextString();
        return time != null ? LocalTime.parse(time, formatter) : null;
    }
}
