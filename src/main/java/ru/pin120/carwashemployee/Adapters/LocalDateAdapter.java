package ru.pin120.carwashemployee.Adapters;

import com.google.gson.*;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


/**
 * ���������� ����������� JsonSerializer � JsonDeserializer ��� �������� ���� LocalDate,
 * �������������� ������������ � �������������� LocalDate �/�� ������� JSON.
 */
public class LocalDateAdapter implements JsonSerializer<LocalDate>, JsonDeserializer<LocalDate> {

    /**
     * ��������� ���� � ������� ��� �������������� �������� ���� LocalDate � ��������� ������������� ISO-8601.
     */
    private static final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;

    /**
     * ����������� ������ ���� LocalDate � JsonElement.
     *
     * @param src       ������ ���� LocalDate ��� ������������.
     * @param typeOfSrc ��� ��������� ������� (�� ������������ � ������ ���������).
     * @param context   �������� ������������.
     * @return ��������������� JsonElement, �������������� ������ LocalDate.
     */
    @Override
    public JsonElement serialize(LocalDate src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.format(formatter));
    }

    /**
     * ������������� JsonElement � ������ ���� LocalDate.
     *
     * @param json     JsonElement, ���������� ���� � ���� ������.
     * @param typeOfT  ��� �������� ������� (�� ������������ � ������ ���������).
     * @param context  �������� ��������������.
     * @return ����������������� ������ ���� LocalDate.
     * @throws JsonParseException ���� JSON ������ �� ������� ������������� � LocalDate.
     */
    @Override
    public LocalDate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return LocalDate.parse(json.getAsString(), formatter);
    }
}
