package ru.yofik.athena.messenger.context.user.integration.auth;

import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yofik.athena.common.AuthV1Response;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;
import ru.yofik.athena.messenger.api.exception.AuthenticationException;
import ru.yofik.athena.messenger.api.exception.ForbiddenException;
import ru.yofik.athena.messenger.context.user.integration.UserApi;
import ru.yofik.athena.messenger.context.user.integration.auth.request.ActivateUserRequest;
import ru.yofik.athena.messenger.context.user.integration.auth.request.AuthorizeUserRequest;
import ru.yofik.athena.messenger.context.user.integration.auth.response.NewAccessTokenResponse;
import ru.yofik.athena.messenger.context.user.model.User;
import ru.yofik.athena.messenger.infrastructure.restApi.AbstractRestTemplateApi;

import java.net.URI;
import java.util.List;

@Service
@Log4j2
public class AuthUserApi extends AbstractRestTemplateApi implements UserApi {
    @Value("${yofik.api.auth-host}")
    private String authHost;
    @Value("${yofik.api.auth-port}")
    private int authPort;


    @Override
    public User authorizeUser(char[] accessToken, char[] clientToken) {
        var response = executeRestTemplate(
                createURI("/api/v1/users/authorities"),
                HttpMethod.POST,
                clientToken,
                new AuthorizeUserRequest(new String(accessToken))
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return AuthV1ResponseParser.parsePayload(authV1Response, User.class);
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't authorize user");
    }

    @Override
    public NewAccessTokenResponse activateUser(String invitation, char[] clientToken) {
        var response = executeRestTemplate(
                createURI("/api/v1/users/invitations"),
                HttpMethod.POST,
                clientToken,
                new ActivateUserRequest(invitation)
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            return AuthV1ResponseParser.parsePayload(authV1Response, NewAccessTokenResponse.class);
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't activate user");
    }

    @Override
    public List<User> getAllUsers(char[] clientToken) {
        var response = executeRestTemplate(
                createURI("/api/v1/users"),
                HttpMethod.GET,
                clientToken,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return (List<User>) AuthV1ResponseParser.parsePayload(authV1Response, new TypeToken<List<User>>(){});
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get all users");
    }

    @Override
    public User getUser(long id, char[] clientToken) {
        var response = executeRestTemplate(
                createURI("/api/v1/users/" + id),
                HttpMethod.GET,
                clientToken,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return AuthV1ResponseParser.parsePayload(authV1Response, User.class);
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get user: " + id);
    }

    @Override
    public void iAmTeapot(char[] clientToken) {
        var response = executeRestTemplate(
                createURI("/api/v1/teapot"),
                HttpMethod.GET,
                clientToken,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            log.warn(() -> "Can't be teapot");
            throw new RuntimeException("Can't be teapot");
        }
    }

    private AuthV1Response getAuthV1Response(ResponseEntity<String> response) {
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

    private URI createURI(String resource) {
        return URI.create(String.format("https://%s:%d/%s", authHost, authPort, resource));
    }
}
