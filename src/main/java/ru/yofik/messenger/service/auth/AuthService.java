package ru.yofik.messenger.service.auth;

import org.springframework.stereotype.Service;
import ru.yofik.messenger.api.request.v1.RegisterUserV1Request;
import ru.yofik.messenger.api.view.AccessView;
import ru.yofik.messenger.model.user.Access;
import ru.yofik.messenger.model.user.User;

import java.util.Optional;

@Service
public interface AuthService {
    AccessView register(RegisterUserV1Request request);
    Optional<User> identifyUser(Access access);
}
