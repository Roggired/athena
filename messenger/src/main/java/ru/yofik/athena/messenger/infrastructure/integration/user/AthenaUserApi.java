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
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository.CrudUserLastOnlineRecordRepository;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@Log4j2
public class AthenaUserApi extends AthenaAbstractApi implements UserRepository {
    private final AthenaCredentialsProvider athenaCredentialsProvider;
    private final CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository;

    public AthenaUserApi(
            AthenaCredentialsProvider athenaCredentialsProvider,
            CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository
    ) {
        this.athenaCredentialsProvider = athenaCredentialsProvider;
        this.crudUserLastOnlineRecordRepository = crudUserLastOnlineRecordRepository;
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
            var page = (Page<User>) AuthV1ResponseParser.parsePayload(authV1Response, new TypeToken<Page<User>>(){});
            fillLastOnlineTime(page);
            return page;
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get page of users");
    }

    private void fillLastOnlineTime(Page<User> page) {
        var userLastOnlineRecords = crudUserLastOnlineRecordRepository.findAllByUserIds(
                page.getContent()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
        var users = page.getContent();
        users.forEach(user -> {
            var userLastOnlineRecord = userLastOnlineRecords.stream()
                    .filter(record -> record.getUserId() == user.getId())
                    .findFirst();

            if (userLastOnlineRecord.isEmpty()) {
                user.setLastOnlineTime(LocalDateTime.MIN);
                return;
            }

            user.setLastOnlineTime(userLastOnlineRecord.get().getLastOnlineTime());
        });
    }

    private void fillLastOnlineTime(User user) {
        var userLastOnlineRecord = crudUserLastOnlineRecordRepository.findByUserId(user.getId());

        if (userLastOnlineRecord.isPresent()) {
            user.setLastOnlineTime(userLastOnlineRecord.get().getLastOnlineTime());
        } else {
            user.setLastOnlineTime(LocalDateTime.MIN);
        }
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
            var user = AuthV1ResponseParser.parsePayload(authV1Response, User.class);
            fillLastOnlineTime(user);

            return user;
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

        var user = (User) authentication.getDetails();
        fillLastOnlineTime(user);
        return user;
    }
}
