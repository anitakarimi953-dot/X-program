package server;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.time.LocalDateTime;

public class LocalDateTimeAdapter
        implements JsonSerializer<LocalDateTime>,
        JsonDeserializer<LocalDateTime> {

    @Override
    public com.google.gson.JsonElement serialize(
            LocalDateTime src,
            java.lang.reflect.Type typeOfSrc,
            com.google.gson.JsonSerializationContext context
    ) {

        return new com.google.gson.JsonPrimitive(
                src.toString()
        );
    }

    @Override
    public LocalDateTime deserialize(
            com.google.gson.JsonElement json,
            java.lang.reflect.Type typeOfT,
            com.google.gson.JsonDeserializationContext context
    ) {

        return LocalDateTime.parse(
                json.getAsString()
        );
    }
}