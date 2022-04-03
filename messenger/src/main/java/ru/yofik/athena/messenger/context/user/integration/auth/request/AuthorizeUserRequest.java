package ru.yofik.athena.messenger.context.user.integration.auth.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthorizeUserRequest {
    public final String accessToken;
}
