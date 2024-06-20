package ru.pin120.carwashemployee.Adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ���������� ����������� JsonSerializer � JsonDeserializer ��� �������� ���� LocalDateTime,
 * �������������� ������������ � �������������� LocalDateTime �/�� ������� JSON.
 */
public class LocalDateTimeAdapter implements JsonSerializer<LocalDateTime>, JsonDeserializer<LocalDateTime> {

    /**
     * ��������� ���� � ������� ��� �������������� �������� ���� LocalDateTime � ��������� ������������� ISO-8601.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    /**
     * ������������� JsonElement � ������ ���� LocalDateTime.
     *
     * @param jsonElement                JsonElement, ���������� ���� � ����� � ���� ������.
     * @param type                       ��� �������� ������� (�� ������������ � ������ ���������).
     * @param jsonDeserializationContext �������� ��������������.
     * @return ����������������� ������ ���� LocalDateTime.
     * @throws JsonParseException ���� JSON ������ �� ������� ������������� � LocalDateTime.
     */
    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        return LocalDateTime.parse(jsonElement.getAsString(), formatter);
    }

    /**
     * ����������� ������ ���� LocalDateTime � JsonElement.
     *
     * @param localDateTime          ������ ���� LocalDateTime ��� ������������.
     * @param type                   ��� ��������� ������� (�� ������������ � ������ ���������).
     * @param jsonSerializationContext �������� ������������.
     * @return ��������������� JsonElement, �������������� ������ LocalDateTime.
     */
    @Override
    public JsonElement serialize(LocalDateTime localDateTime, Type type, JsonSerializationContext jsonSerializationContext) {
        return new JsonPrimitive(localDateTime.format(formatter));
    }
}
