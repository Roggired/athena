package ru.yofik.athena.auth.domain.auth.model;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class RefreshJwtPayload {
    public final Long userId;
    public final String sessionId;
    public final Long exp;
    public final Long iat;
    public final String typ = JwtPurpose.REFRESH.name();
}
