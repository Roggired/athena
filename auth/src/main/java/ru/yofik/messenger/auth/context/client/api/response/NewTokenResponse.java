package ru.yofik.messenger.auth.context.client.api.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NewTokenResponse {
    public final String token;
}
