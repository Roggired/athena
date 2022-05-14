package ru.yofik.athena.messenger.infrastructure.integration.user;

import com.google.gson.reflect.TypeToken;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;
import ru.yofik.athena.messenger.infrastructure.integration.AthenaAbstractApi;
import ru.yofik.athena.messenger.infrastructure.integration.AthenaCredentialsProvider;

import java.util.List;

@Component
@Log4j2
public class AthenaUserApi extends AthenaAbstractApi implements UserRepository {
    private final AthenaCredentialsProvider athenaCredentialsProvider;

    public AthenaUserApi(AthenaCredentialsProvider athenaCredentialsProvider) {
        this.athenaCredentialsProvider = athenaCredentialsProvider;
    }

    @Override
    public List<User> getAllUsers() {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/users"),
                HttpMethod.GET,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
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
    public Page<User> getPage(Page.Meta pageMeta) {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/users/pages", pageMeta),
                HttpMethod.GET,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return (Page<User>) AuthV1ResponseParser.parsePayload(authV1Response, new TypeToken<Page<User>>(){});
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get page of users");
    }

    @Override
    public User getUser(long id) {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/users/" + id),
                HttpMethod.GET,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
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
    public User updateUser(User user) {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/users/" + user.getId()),
                HttpMethod.PUT,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
                user
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            log.warn(() -> "Can't update user with id: " + user.getId());
            throw new RuntimeException("Can't update user");
        }

        return AuthV1ResponseParser.parsePayload(authV1Response, User.class);
    }

    @Override
    public User getCurrentUser() {
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();

        if (authentication.getDetails() == null) {
            log.fatal("Athena User Api cannot provide user. Authentication details is null");
            throw new RuntimeException();
        }

        return (User) authentication.getDetails();
    }
}
