package ru.yofik.athena.admin.infrastructure.restApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import ru.yofik.athena.admin.api.exception.AuthenticationException;
import ru.yofik.athena.admin.api.exception.ForbiddenException;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseParser;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;

import java.net.URI;

public abstract class AbstractAuthServiceApi extends AbstractRestTemplateApi {
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
