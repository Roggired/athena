package ru.yofik.athena.messenger.context.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.messenger.context.user.view.AccessTokenView;
import ru.yofik.athena.messenger.context.user.view.UserView;

import java.util.List;

@Service
public interface UserService {
    AccessTokenView activate(ActivateUserRequest request, String deviceId);
    UserView authorizeUser(String accessToken, String deviceId);
    List<UserView> getAllUsers();
    UserView getUser(long id);
}
