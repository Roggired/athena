package ru.yofik.athena.messenger.infrastructure.integration.auth.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorizeUserRequest {
    public final String accessToken;
}
