package ru.pin120.carwashemployee.Boxes;

import com.google.gson.*;

import java.lang.reflect.Type;

public class BoxStatusDeserializer implements JsonDeserializer<BoxStatus> {

    @Override
    public BoxStatus deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        String displayValue = jsonObject.get("displayValue").getAsString();
        //return BoxStatus.valueOfDisplayValue(displayValue);
        return BoxStatus.AVAILABLE;
    }

}
