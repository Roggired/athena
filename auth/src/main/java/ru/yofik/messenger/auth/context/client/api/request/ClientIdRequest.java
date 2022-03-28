package ru.yofik.messenger.auth.context.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Positive;

@Getter
@AllArgsConstructor
public class ClientIdRequest {
    @Positive
    public final long id;
}
