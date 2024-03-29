package ru.yofik.athena.auth.infrastructure.response;

import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@ToString
public class AuthV1Response {
    public final int httpStatusCode;
    public final String status;
    public final Object payload;


    public static AuthV1Response of(AuthV1ResponseStatus authV1ResponseStatus, Object payload) {
        return new AuthV1Response(
                authV1ResponseStatus.getHttpStatusCode(),
                authV1ResponseStatus.getStatus(),
                payload
        );
    }
}
