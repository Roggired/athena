package ru.yofik.athena.admin.context.client.integration.auth;

import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import ru.yofik.athena.admin.context.client.integration.ClientApi;
import ru.yofik.athena.admin.context.client.integration.auth.request.CreateClientAuthRequest;
import ru.yofik.athena.admin.context.client.integration.auth.response.NewTokenAuthResponse;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.ClientPermission;
import ru.yofik.athena.admin.context.client.model.Token;
import ru.yofik.athena.admin.infrastructure.restApi.AbstractAuthServiceApi;
import ru.yofik.athena.common.api.AuthV1ResponseParser;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@Log4j2
public class AuthServiceClientApiImpl extends AbstractAuthServiceApi implements ClientApi {
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
                new CreateClientAuthRequest(
                        client.getName(),
                        client.getClientPermissions()
                                .stream()
                                .map(ClientPermission::toString)
                                .collect(Collectors.toSet())
                )
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
}
