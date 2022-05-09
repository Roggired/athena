package ru.yofik.athena.messenger.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import ru.yofik.athena.common.AuthV1Response;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;
import ru.yofik.athena.messenger.api.exception.AuthenticationException;
import ru.yofik.athena.messenger.api.exception.ForbiddenException;

import java.net.URI;

public abstract class AthenaAbstractApi extends AbstractRestTemplateApi {
    @Value("${yofik.api.auth-host}")
    private String authHost;
    @Value("${yofik.api.auth-port}")
    private int authPort;

    protected AuthV1Response getAuthV1Response(ResponseEntity<String> response) {
        if (response.getBody() != null) {
            var authV1Response = AuthV1ResponseParser.fromJson(response.getBody());

            if (authV1Response.status.equals(AuthV1ResponseStatus.UNAUTHENTICATED.getStatus())) {
                throw new AuthenticationException();
            }

            if (authV1Response.status.equals(AuthV1ResponseStatus.NOT_HAVE_PERMISSION.getStatus())) {
                throw new ForbiddenException();
            }

            return authV1Response;
        }

        throw new RuntimeException("Empty response body");
    }

    protected URI createURI(String resource) {
        return URI.create(String.format("https://%s:%d/%s", authHost, authPort, resource));
    }
}
