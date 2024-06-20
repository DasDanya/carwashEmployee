package ru.pin120.carwashemployee.Adapters;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/**
 * Адаптер для сериализации и десериализации объектов типа LocalTime в формат JSON и обратно.
 */
public class LocalTimeAdapter extends TypeAdapter<LocalTime> {

    /**
     * Форматтер времени для преобразования LocalTime в строку в формате "HH:mm".
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

    /**
     * Записывает объект типа LocalTime в формат JSON.
     *
     * @param jsonWriter Писатель JSON.
     * @param localTime  Объект типа LocalTime для записи.
     * @throws IOException Если происходит ошибка ввода-вывода при записи в JSON.
     */

    @Override
    public void write(JsonWriter jsonWriter, LocalTime localTime) throws IOException {
        jsonWriter.value(localTime != null ? localTime.format(formatter) : null);
    }

    /**
     * Считывает объект типа LocalTime из формата JSON.
     *
     * @param jsonReader Читатель JSON.
     * @return Десериализованный объект типа LocalTime.
     * @throws IOException Если происходит ошибка ввода-вывода при чтении из JSON.
     */
    @Override
    public LocalTime read(JsonReader jsonReader) throws IOException {
        String time = jsonReader.nextString();
        return time != null ? LocalTime.parse(time, formatter) : null;
    }
}
