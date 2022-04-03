package ru.yofik.athena.admin.context.client.integration.auth;

import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.yofik.athena.admin.api.exception.AuthenticationException;
import ru.yofik.athena.admin.api.exception.ForbiddenException;
import ru.yofik.athena.admin.context.client.integration.ClientApi;
import ru.yofik.athena.admin.context.client.integration.auth.request.CreateClientAuthRequest;
import ru.yofik.athena.admin.context.client.integration.auth.request.LoginRequest;
import ru.yofik.athena.admin.context.client.integration.auth.response.NewTokenAuthResponse;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.Token;
import ru.yofik.athena.admin.infrastructure.restApi.AbstractRestTemplateApi;
import ru.yofik.athena.common.AuthV1Response;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Component
@Log4j2
public class AuthServiceClientApi extends AbstractRestTemplateApi implements ClientApi {
    @Value("${yofik.api.auth-host}")
    private String authHost;
    @Value("${yofik.api.auth-port}")
    private int authPort;

    @Override
    public Optional<Client> findById(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients/" + id),
                HttpMethod.GET,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return Optional.of(AuthV1ResponseParser.parsePayload(authV1Response, Client.class));
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        return Optional.empty();
    }

    @Override
    public List<Client> findAll(char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients"),
                HttpMethod.GET,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return (List<Client>) AuthV1ResponseParser.parsePayload(authV1Response, new TypeToken<List<Client>>(){});
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        return Collections.emptyList();
    }

    @Override
    public Token create(Client client, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients"),
                HttpMethod.POST,
                token,
                new CreateClientAuthRequest(client.getName(), client.getClientPermissions())
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return new Token(AuthV1ResponseParser.parsePayload(authV1Response, NewTokenAuthResponse.class).token.toCharArray());
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't create new client");
    }

    @Override
    public Token generateNewToken(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients/" + id + "/tokens"),
                HttpMethod.POST,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return new Token(AuthV1ResponseParser.parsePayload(authV1Response, NewTokenAuthResponse.class).token.toCharArray());
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't create new client");
    }

    @Override
    public void setActivateTo(long id, boolean newValue, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients/" + id + (newValue ? "/activation" : "/deactivation")),
                HttpMethod.PUT,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            log.warn(() -> "Auth Service response: " + authV1Response);
            throw new RuntimeException("Can't set client active to : " + newValue);
        }
    }

    @Override
    public void deleteBy(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/clients/" + id),
                HttpMethod.DELETE,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            log.warn(() -> "Auth Service response: " + authV1Response);
            throw new RuntimeException("Can't delete client: " + id);
        }
    }

    @Override
    public Token login(String password) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/login"),
                HttpMethod.POST,
                new LoginRequest(password)
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return new Token(AuthV1ResponseParser.parsePayload(authV1Response, NewTokenAuthResponse.class).token.toCharArray());
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't login admin");
    }

    @Override
    public void iAmTeapot(char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/teapot"),
                HttpMethod.GET,
                token,
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
