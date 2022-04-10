package ru.yofik.athena.messenger.infrastructure.wsApi;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.AllArgsConstructor;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AthenaWSMessage {
    private final AthenaWSCommand command;
    private final Object argument;


    public <T> T getArgumentAs(Class<T> clazz) {
        return new Gson().fromJson((String) argument, clazz);
    }

    public <T> Object getArgumentAs(TypeToken<T> typeToken) {
        return new Gson().fromJson((String) argument, typeToken.getType());
    }
}
