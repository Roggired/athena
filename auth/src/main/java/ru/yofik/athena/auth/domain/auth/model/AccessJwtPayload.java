package ru.yofik.athena.auth.domain.auth.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AccessJwtPayload {
    public final long userId;
    public final String sessionId;
    public final String role;
    public final Long exp;
    public final Long iat;
    public final String typ = JwtPurpose.ACCESS.name();
}
