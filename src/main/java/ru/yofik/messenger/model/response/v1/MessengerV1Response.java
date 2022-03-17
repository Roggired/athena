package ru.yofik.messenger.model.response.v1;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public abstract class MessengerV1Response {
    private final int status;
    private final Object payload;
}
