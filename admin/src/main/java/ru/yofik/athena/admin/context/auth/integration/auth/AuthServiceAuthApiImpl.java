package ru.yofik.athena.admin.context.auth.integration.auth;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import ru.yofik.athena.admin.context.auth.integration.AuthApi;
import ru.yofik.athena.admin.context.client.integration.auth.request.LoginRequest;
import ru.yofik.athena.admin.context.client.integration.auth.response.NewTokenAuthResponse;
import ru.yofik.athena.admin.context.client.model.Token;
import ru.yofik.athena.admin.infrastructure.restApi.AbstractAuthServiceApi;
import ru.yofik.athena.common.AuthV1ResponseParser;
import ru.yofik.athena.common.AuthV1ResponseStatus;

@Service
@Log4j2
public class AuthServiceAuthApiImpl extends AbstractAuthServiceApi implements AuthApi {
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
}
