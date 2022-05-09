package ru.yofik.athena.messenger.infrastructure.integration.auth.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ActivateUserRequest {
    public final String code;
}
