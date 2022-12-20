package ru.yofik.athena.messenger.infrastructure.integration.auth;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UpdateUserRequest {
    public final String login;
    public final String email;
}

