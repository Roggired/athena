package ru.yofik.athena.messenger.context.user.integration.auth.request;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ActivateUserRequest {
    public final String code;
}
