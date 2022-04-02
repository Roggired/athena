package ru.yofik.athena.common;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Objects;

public final class AuthV1ResponseParser {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AuthV1Response.class, new AuthV1ResponseJsonDeserializer())
            .create();


    public static boolean isStatus(AuthV1Response authV1Response, AuthV1ResponseStatus expectedStatus) {
        return Objects.equals(authV1Response.status, expectedStatus.getStatus());
    }

    public static <T> T parsePayload(AuthV1Response authV1Response, Class<T> payloadType) {
        return GSON.fromJson((String) authV1Response.payload, payloadType);
    }

    public static Object parsePayload(AuthV1Response authV1Response, TypeToken<?> type) {
        return GSON.fromJson((String) authV1Response.payload, type.getType());
    }

    public static AuthV1Response fromJson(String json) {
        return GSON.fromJson(json, AuthV1Response.class);
    }


    private static class AuthV1ResponseJsonDeserializer implements JsonDeserializer<AuthV1Response> {
        @Override
        public AuthV1Response deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            int httpStatus = jsonObject.get("httpStatusCode").getAsInt();
            String status = jsonObject.get("status").getAsString();
            String jsonPayload = jsonObject.get("payload").toString();

            return new AuthV1Response(
                    httpStatus,
                    status,
                    jsonPayload
            );
        }
    }
}
