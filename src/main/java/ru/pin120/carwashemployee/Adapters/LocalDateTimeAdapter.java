package ru.pin120.carwashemployee.Adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Реализация интерфейсов JsonSerializer и JsonDeserializer для объектов типа LocalDateTime,
 * обеспечивающая сериализацию и десериализацию LocalDateTime в/из формата JSON.
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /**
     * Форматтер даты и времени для форматирования объектов типа LocalDateTime в строковое представление ISO-8601.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * Десериализует JsonElement в объект типа LocalDateTime.
     *
     * @param jsonElement                JsonElement, содержащий дату и время в виде строки.
     * @param type                       Тип целевого объекта (не используется в данном контексте).
     * @param jsonDeserializationContext Контекст десериализации.
     * @return Десериализованный объект типа LocalDateTime.
     * @throws JsonParseException Если JSON строку не удается преобразовать в LocalDateTime.
     */
    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), formatter);
    }

    /**
     * Сериализует объект типа LocalDateTime в JsonElement.
     *
     * @param localDateTime          Объект типа LocalDateTime для сериализации.
     * @param type                   Тип исходного объекта (не используется в данном контексте).
     * @param jsonSerializationContext Контекст сериализации.
     * @return Сериализованный JsonElement, представляющий объект LocalDateTime.
     */
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDateTime.format(formatter));
    }
}
