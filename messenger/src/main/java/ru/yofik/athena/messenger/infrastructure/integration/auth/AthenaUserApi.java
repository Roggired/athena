package ru.yofik.athena.messenger.infrastructure.integration.auth;

import com.google.gson.reflect.TypeToken;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import retrofit2.Response;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseParser;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.common.security.SecurityUtils;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository.CrudUserLastOnlineRecordRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Component
@Slf4j
public class AthenaUserApi implements UserRepository {
    private final CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository;
    private final AuthUserApi authUserApi;

    public AthenaUserApi(
            CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository,
            AuthUserApi authUserApi) {
        this.crudUserLastOnlineRecordRepository = crudUserLastOnlineRecordRepository;
        this.authUserApi = authUserApi;
    }

    @Override
    public NewPage<User> getPage(NewPage.Meta pageMeta, FilteredUsersRequest request) {
        Response<AuthV1Response> response;
        try {
            response = authUserApi.getUsersFiltered(
                    pageMeta.sequentialNumber,
                    pageMeta.size,
                    request,
                    "Bearer " + SecurityUtils.getAuthenticatedUserAccessToken()
            ).execute();
        } catch (IOException e) {
            throw new AuthServiceIntegrationException("Unexpected exception during requesting " + AuthServiceEndpoints.USERS_GET_FILTERED.fullUrl(), e);
        }

        var authV1Response = extractAuthV1Response(response, AuthServiceEndpoints.USERS_GET_FILTERED.fullUrl());

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            return (NewPage<User>) AuthV1ResponseParser.parsePayloadForRetrofit(authV1Response, new TypeToken<NewPage<User>>(){});
        }

        log.warn("Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get page of users");
    }

    private AuthV1Response extractAuthV1Response(Response<AuthV1Response> retrofitResponse, String errorEndpointAppendix) {
        if (!retrofitResponse.isSuccessful()) {
            try(var responseBody = retrofitResponse.errorBody()) {
                if (responseBody == null) {
                    throw new AuthServiceIntegrationException("Unexpected exception during requesting: " + errorEndpointAppendix + ". HTTP status: " + retrofitResponse.code() + " can't extract error body");
                }

                var errorAsString = responseBody.string();
                throw new AuthServiceIntegrationException("Unexpected exception during requesting: " + errorEndpointAppendix + ". HTTP status: " + retrofitResponse.code() + " errorBody as string: " + errorAsString);
            } catch (IOException e) {
                throw new AuthServiceIntegrationException("Unexpected exception during requesting: " + errorEndpointAppendix + ". HTTP status: " + retrofitResponse.code() + " exception during trying to read error body: ", e);
            }
        }

        return retrofitResponse.body();
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
        Response<AuthV1Response> response;
        try {
            response = authUserApi.getUserById(
                    id,
                    "Bearer " + SecurityUtils.getAuthenticatedUserAccessToken()
            ).execute();
        } catch (IOException e) {
            throw new AuthServiceIntegrationException("Unexpected exception during requesting " + AuthServiceEndpoints.USERS_GET_BY_ID.fullUrl(), e);
        }

        var authV1Response = extractAuthV1Response(response, AuthServiceEndpoints.USERS_GET_BY_ID.fullUrl());

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            var user = AuthV1ResponseParser.parsePayloadForRetrofit(authV1Response, User.class);
            fillLastOnlineTime(user);

            return user;
        }

        log.warn("Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't get user: " + id);
    }

    @Override
    public User updateUser(User user) {
        Response<AuthV1Response> response;
        try {
            response = authUserApi.updateUserById(
                    user.getId(),
                    new UpdateUserRequest(
                            user.getLogin(),
                            user.getEmail()
                    ),
                    "Bearer " + SecurityUtils.getAuthenticatedUserAccessToken()
            ).execute();
        } catch (IOException e) {
            throw new AuthServiceIntegrationException("Unexpected exception during requesting " + AuthServiceEndpoints.USERS_UPDATE.fullUrl(), e);
        }

        var authV1Response = extractAuthV1Response(response, AuthServiceEndpoints.USERS_UPDATE.fullUrl());

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_UPDATED)) {
            return AuthV1ResponseParser.parsePayloadForRetrofit(authV1Response, User.class);
        }

        log.warn("Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't update user: " + user.getId());
    }

    @Override
    public User getCurrentUser() {
        Response<AuthV1Response> response;
        try {
            response = authUserApi.getMyUser(
                    "Bearer " + SecurityUtils.getAuthenticatedUserAccessToken()
            ).execute();
        } catch (IOException e) {
            throw new AuthServiceIntegrationException("Unexpected exception during requesting " + AuthServiceEndpoints.USERS_GET_MY.fullUrl(), e);
        }

        var authV1Response = extractAuthV1Response(response, AuthServiceEndpoints.USERS_GET_MY.fullUrl());

        if (AuthV1ResponseParser.isStatus(authV1Response, AuthV1ResponseStatus.RESOURCE_RETURNED)) {
            var user = AuthV1ResponseParser.parsePayloadForRetrofit(authV1Response, User.class);
            fillLastOnlineTime(user);
            return user;
        }

        log.warn("Auth Service response: " + authV1Response);
        throw new RuntimeException("Can't my user: " + SecurityUtils.getAuthenticatedUserId());
    }
}
