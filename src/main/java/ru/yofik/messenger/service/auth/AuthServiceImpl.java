package ru.yofik.messenger.service.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yofik.messenger.model.user.Access;
import ru.yofik.messenger.model.user.User;
import ru.yofik.messenger.api.request.v1.RegisterUserV1Request;
import ru.yofik.messenger.api.view.AccessView;
import ru.yofik.messenger.service.exception.ElementAlreadyExistsException;
import ru.yofik.messenger.storage.dao.UserDao;

import java.util.Optional;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final UserDao userDao;
    private final JwtTokenFactory jwtTokenFactory;
    private final JwtTokenParser jwtTokenParser;


    public AuthServiceImpl(UserDao userDao, JwtTokenFactory jwtTokenFactory, JwtTokenParser jwtTokenParser) {
        this.userDao = userDao;
        this.jwtTokenFactory = jwtTokenFactory;
        this.jwtTokenParser = jwtTokenParser;
    }


    public AccessView register(RegisterUserV1Request request) {
        if (userDao.isUserExistsByLogin(request.getLogin())) {
            log.warn("User with such login: " + request.getLogin() + " already exists");
            throw new ElementAlreadyExistsException();
        }

        var user = User.of(request.getLogin(), request.getPassword());
        var generatedId = userDao.create(user);
        log.info("Created new user with id: " + generatedId);

        var access = jwtTokenFactory.generate(generatedId);
        log.info("New access: " + access);
        return new AccessView(access.getAccessToken());
    }

    public Optional<User> identifyUser(Access access) {
        if (!jwtTokenParser.validate(access.getAccessToken())) {
            log.warn("Wrong access token");
            return Optional.empty();
        }

        return userDao.findUserById(jwtTokenParser.getUserId(access.getAccessToken()));
    }
}
