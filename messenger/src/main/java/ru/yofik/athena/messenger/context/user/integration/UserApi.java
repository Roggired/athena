package ru.yofik.athena.messenger.context.user.integration;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.user.integration.auth.response.NewAccessTokenResponse;
import ru.yofik.athena.messenger.context.user.model.User;

import java.util.List;

@Service
public interface UserApi {
    User authorizeUser(char[] accessToken, char[] clientToken);
    NewAccessTokenResponse activateUser(String invitation, char[] clientToken);
    List<User> getAllUsers(char[] clientToken);
    User getUser(long id, char[] clientToken);
    void iAmTeapot(char[] clientToken);
}
