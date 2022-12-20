package ru.yofik.athena.common.api;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthV1Response {
    public int httpStatusCode;
    public String status;
    public Object payload;

    public AuthV1Response() {}

    public static AuthV1Response of(AuthV1ResponseStatus authV1ResponseStatus, Object payload) {
        return new AuthV1Response(
                authV1ResponseStatus.getHttpStatusCode(),
                authV1ResponseStatus.getStatus(),
                payload
        );
    }
}
