package ru.yofik.athena.auth.domain.auth.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserTokenAccess {
    public final String accessToken;
    public final long expiresIn;
    public final String refreshToken;
}
