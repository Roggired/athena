package ru.yofik.athena.messenger.context.user.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.messenger.context.user.api.request.UpdateUserRequest;
import ru.yofik.athena.messenger.context.user.integration.UserApi;
import ru.yofik.athena.messenger.context.user.model.AccessToken;
import ru.yofik.athena.messenger.context.user.model.User;
import ru.yofik.athena.messenger.context.user.view.AccessTokenView;
import ru.yofik.athena.messenger.context.user.view.UserView;
import ru.yofik.athena.messenger.infrastructure.service.AbstractService;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
public class UserServiceImpl extends AbstractService implements UserService {
    @Value("${yofik.security.client-token}")
    private String clientTokenString;
    private char[] clientToken;


    @PostConstruct
    public void init() {
        clientToken = clientTokenString.toCharArray();
        clientTokenString = null;
    }

    private final UserApi userApi;

    public UserServiceImpl(UserApi userApi) {
        this.userApi = userApi;
    }

    @Override
    public AccessTokenView activate(ActivateUserRequest request, String deviceId) {
        var response = userApi.activateUser(request.invitation, clientToken, deviceId);
        return new AccessToken(response.accessToken.toCharArray()).toView();
    }

    @Override
    public UserView authorizeUser(String accessToken, String deviceId) {
        return userApi.authorizeUser(accessToken.toCharArray(), clientToken, deviceId).toView();
    }

    @Override
    public List<UserView> getAllUsers() {
        return userApi.getAllUsers(clientToken)
                .stream()
                .map(User::toView)
                .collect(Collectors.toList());
    }

    @Override
    public UserView getUser(long id) {
        var user = userApi.getUser(id, clientToken);
        return new UserView(
                user.getId(),
                user.getName(),
                user.getLogin()
        );
    }

    @Override
    public UserView updateUser(UpdateUserRequest request) {
        var currentUser = getCurrentUser();
        currentUser.setName(request.name);
        currentUser.setLogin(request.login);

        var accessToken = getAccessToken();
        var updatedUser = userApi.updateUser(
                accessToken,
                clientToken,
                currentUser
        );
        return updatedUser.toView();
    }
}
