package ru.yofik.athena.admin.context.user.integration.auth;

import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.yofik.athena.admin.api.exception.AuthenticationException;
import ru.yofik.athena.admin.api.exception.ForbiddenException;
import ru.yofik.athena.admin.context.user.integration.UserApi;
import ru.yofik.athena.admin.context.user.integration.auth.request.CreateUserAuthRequest;
import ru.yofik.athena.admin.context.user.integration.auth.request.NewInvitationRequest;
import ru.yofik.athena.admin.context.user.integration.auth.response.NewInvitationAuthResponse;
import ru.yofik.athena.admin.context.user.model.Invitation;
import ru.yofik.athena.admin.context.user.model.User;
import ru.yofik.athena.admin.context.user.model.UserInfo;
import ru.yofik.athena.admin.infrastructure.restApi.AbstractRestTemplateApi;
import ru.yofik.athena.common.AuthV1Response;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Log4j2
public class AuthServiceUserApi extends AbstractRestTemplateApi implements UserApi {
    @Value("${yofik.api.auth-host}")
    private String authHost;
    @Value("${yofik.api.auth-port}")
    private int authPort;


    @Override
    public Optional<User> findById(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users/" + id),
                HttpMethod.GET,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return Optional.of(AuthV1ResponseParser.parsePayload(authV1Response, User.class));
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        return Optional.empty();
    }

    @Override
    public List<UserInfo> findAll(char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users"),
                HttpMethod.GET,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return (List<UserInfo>) AuthV1ResponseParser.parsePayload(authV1Response, new TypeToken<List<UserInfo>>(){});
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        return Collections.emptyList();
    }

    @Override
    public void deleteUser(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users/" + id),
                HttpMethod.DELETE,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_DELETED)) {
            log.warn(() -> "Auth Service response: " + authV1Response);
            throw new RuntimeException("Can't delete user: " + id);
        }
    }

    @Override
    public void lockUser(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users/" + id + "/lock"),
                HttpMethod.POST,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            log.warn(() -> "Auth Service response: " + authV1Response);
            throw new RuntimeException("Can't lock user");
        }
    }

    @Override
    public void unlockUser(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users/" + id + "/lock"),
                HttpMethod.DELETE,
                token,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            log.warn(() -> "Auth Service response: " + authV1Response);
            throw new RuntimeException("Can't unlock user");
        }
    }

    @Override
    public void createUser(User user, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users"),
                HttpMethod.POST,
                token,
                new CreateUserAuthRequest(user.getName(), user.getLogin())
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_CREATED)) {
            return;
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't create new user");
    }

    @Override
    public Invitation createNewInvitation(long id, char[] token) {
        var response = executeRestTemplate(
                createURI("/api/v1/admin/users/" + id + "/invitations"),
                HttpMethod.PUT,
                token,
                new NewInvitationRequest(1)
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_CREATED)) {
            var newInvitation = AuthV1ResponseParser.parsePayload(authV1Response, NewInvitationAuthResponse.class);
            return new Invitation(
                    newInvitation.getCode(),
                    newInvitation.getCount()
            );
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't create new invitation");
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
