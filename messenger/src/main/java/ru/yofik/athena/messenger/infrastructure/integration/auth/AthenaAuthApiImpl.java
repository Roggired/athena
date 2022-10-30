package ru.yofik.athena.messenger.infrastructure.integration.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import ru.yofik.athena.common.api.AuthV1ResponseParser;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.messenger.domain.user.model.AccessToken;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.integration.AthenaAbstractApi;
import ru.yofik.athena.messenger.infrastructure.integration.AthenaCredentialsProvider;
import ru.yofik.athena.messenger.infrastructure.integration.auth.request.ActivateUserRequest;
import ru.yofik.athena.messenger.infrastructure.integration.auth.request.AuthorizeUserRequest;
import ru.yofik.athena.messenger.infrastructure.integration.auth.response.NewAccessTokenResponse;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.entity.UserLastOnlineRecord;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository.CrudUserLastOnlineRecordRepository;
import ru.yofik.athena.messenger.utils.DateUtils;

@Component
@Log4j2
public class AthenaAuthApiImpl extends AthenaAbstractApi implements AthenaAuthApi {
    private final AthenaCredentialsProvider athenaCredentialsProvider;
    private final CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository;

    public AthenaAuthApiImpl(
            AthenaCredentialsProvider athenaCredentialsProvider,
            CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository
    ) {
        this.athenaCredentialsProvider = athenaCredentialsProvider;
        this.crudUserLastOnlineRecordRepository = crudUserLastOnlineRecordRepository;
    }

    @Override
    public User authorize(String accessToken) {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/users/authorities"),
                HttpMethod.POST,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
                new AuthorizeUserRequest(accessToken)
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            var user = AuthV1ResponseParser.parsePayload(authV1Response, User.class);

            var userLastOnlineRecord = new UserLastOnlineRecord();
            var lastOnlineTime = DateUtils.nowUTC();
            userLastOnlineRecord.setUserId(user.getId());
            userLastOnlineRecord.setLastOnlineTime(lastOnlineTime);
            crudUserLastOnlineRecordRepository.save(userLastOnlineRecord);

            user.setOnline(true);
            user.setLastOnlineTime(lastOnlineTime);

            return user;
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't authorize user");
    }

    @Override
    public AccessToken activate(String invitation) {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        log.info("Client toekn: " + new String(clientCredentials.clientToken));
        var response = executeRestTemplate(
                createURI("/api/v1/users/invitations"),
                HttpMethod.POST,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
                new ActivateUserRequest(invitation)
        );
        var authV1Response = getAuthV1Response(response);

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            return new AccessToken(
                    AuthV1ResponseParser.parsePayload(authV1Response, NewAccessTokenResponse.class).accessToken.toCharArray()
            );
        }

        log.warn(() -> "Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't activate user");
    }

    @Override
    public void iAmTeapot() {
        var clientCredentials = athenaCredentialsProvider.provideClientCredentials();
        var response = executeRestTemplate(
                createURI("/api/v1/teapot"),
                HttpMethod.GET,
                clientCredentials.clientToken,
                clientCredentials.deviceId,
                null
        );
        var authV1Response = getAuthV1Response(response);

        if (!AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            log.warn(() -> "Can't be teapot");
            throw new RuntimeException("Can't be teapot");
        }
    }
}
