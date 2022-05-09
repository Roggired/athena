package ru.yofik.athena.messenger.infrastructure.integration.auth;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.domain.user.model.AccessToken;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.integration.auth.response.NewAccessTokenResponse;

@Service
public interface AthenaAuthApi {
    User authorize(String accessToken);
    AccessToken activate(String invitation);
    void iAmTeapot();
}
