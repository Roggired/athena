package ru.yofik.athena.auth.domain.auth.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AdminChangePasswordResponse {
    public final long userId;
    public final String changePasswordCode;
    public final long expiresIn;
}
