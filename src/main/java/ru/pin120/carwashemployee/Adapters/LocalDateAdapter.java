package ru.pin120.carwashemployee.Adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * Реализация интерфейсов JsonSerializer и JsonDeserializer для объектов типа LocalDate,
 * обеспечивающая сериализацию и десериализацию LocalDate в/из формата JSON.
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    /**
     * Форматтер даты и времени для форматирования объектов типа LocalDate в строковое представление ISO-8601.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * Сериализует объект типа LocalDate в JsonElement.
     *
     * @param src       Объект типа LocalDate для сериализации.
     * @param typeOfSrc Тип исходного объекта (не используется в данном контексте).
     * @param context   Контекст сериализации.
     * @return Сериализованный JsonElement, представляющий объект LocalDate.
     */
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }

    /**
     * Десериализует JsonElement в объект типа LocalDate.
     *
     * @param json     JsonElement, содержащий дату в виде строки.
     * @param typeOfT  Тип целевого объекта (не используется в данном контексте).
     * @param context  Контекст десериализации.
     * @return Десериализованный объект типа LocalDate.
     * @throws JsonParseException Если JSON строку не удается преобразовать в LocalDate.
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}
