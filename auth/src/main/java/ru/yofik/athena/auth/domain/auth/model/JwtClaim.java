package ru.yofik.athena.auth.domain.auth.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtClaim {
    USER_ID("userId"),
    USER_ROLE("role"),
    USER_SESSION_ID("sessionId"),
    TOKEN_TYPE("typ"),
    EXPIRES_AT("exp"),
    ISSUED_AT("iat"),
    ;

    private final String value;
}
