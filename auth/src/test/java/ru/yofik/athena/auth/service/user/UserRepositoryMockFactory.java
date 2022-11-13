package ru.yofik.athena.auth.service.user;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import ru.yofik.athena.auth.domain.user.model.*;
import ru.yofik.athena.auth.domain.user.repository.UserRepository;
import ru.yofik.athena.auth.utils.TimeUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class UserRepositoryMockFactory {
    private static final Map<Long, User> USER_MAP = Map.of(
            1L,
            new User(
                    1L,
                    "qwerty",
                    Role.USER,
                    new Lock(
                            1L,
                            false,
                            ""
                    ),
                    new Credentials(
                            1L,
                            "q1w1e1-r1t1y1-12345-12345",
                            TimeUtils.now().plusHours(2)
                    ),
                    new Session(
                            1L,
                            "q1w1e1r1t1",
                            LocalDateTime.now().minusHours(1)
                    )
            ),
            2L,
            new User(
                    2L,
                    "adminadmin",
                    Role.ADMIN,
                    new Lock(
                            2L,
                            false,
                            ""
                    ),
                    new Credentials(
                            2L,
                            "1111",
                            TimeUtils.infinity()
                    ),
                    new Session(
                            2L,
                            "zxcvbn",
                            LocalDateTime.now().minusHours(1)
                    )
            ),
            3L,
            new User(
                    3L,
                    "abcde",
                    Role.USER,
                    new Lock(
                            3L,
                            true,
                            "Test"
                    ),
                    new Credentials(
                            3L,
                            "q2w2e2-r2t2y2-12345-12345",
                            TimeUtils.now().plusHours(2)
                    ),
                    new Session(
                            3L,
                            "q2w2e2r2t2",
                            LocalDateTime.now().minusHours(1)
                    )
            )
    );


    public static UserRepository create() {
        var userRepositoryMock = Mockito.mock(UserRepository.class);

        when(userRepositoryMock.findById(any())).then(UserRepositoryMockFactory::returnUserById);

        when(userRepositoryMock.save(any())).then(UserRepositoryMockFactory::returnSavedUser);

        when(userRepositoryMock.findByLogin(any())).then(UserRepositoryMockFactory::returnUserByLogin);

        when(userRepositoryMock.findAllByFilters(
                any(),
                any(),
                any()
        )).then(UserRepositoryMockFactory::returnUsersByFilters);

        return userRepositoryMock;
    }

    private static Optional<User> returnUserById(InvocationOnMock invocation) {
        var id = (Long) invocation.getArgument(0);
        var user = USER_MAP.get(id);

        if (user == null) return Optional.empty();

        return Optional.of(
                new User(
                        user.getId(),
                        user.getLogin(),
                        user.getRole(),
                        user.getLock(),
                        user.getCredentials(),
                        user.getSession()
                )
        );
    }

    private static User returnSavedUser(InvocationOnMock invocation) throws NoSuchAlgorithmException {
        var newUser = (User) invocation.getArgument(0);

        if (newUser.getId() == 0) {
            newUser.getSession().setLastLoginDate(TimeUtils.infinity());
            newUser.getCredentials().setValue(
                    Base64.getEncoder().encodeToString(
                            MessageDigest.getInstance("SHA-512").digest("1111".getBytes(StandardCharsets.UTF_8))
                    )
            );
            newUser.setId(10L);
            return newUser;
        }

        return newUser;
    }

    private static Optional<User> returnUserByLogin(InvocationOnMock invocation) {
        var login = (String) invocation.getArgument(0);
        return USER_MAP.values()
                .stream()
                .filter(user -> user.getLogin().equals(login))
                .findAny()
                .map(user -> new User(
                        user.getId(),
                        user.getLogin(),
                        user.getRole(),
                        user.getLock(),
                        user.getCredentials(),
                        user.getSession()
                ));
    }

    private static Page<User> returnUsersByFilters(InvocationOnMock invocation) {
        var login = (String) invocation.getArgument(0);
        var role = (String) invocation.getArgument(1);
        var pageable = (Pageable) invocation.getArgument(2);

        return new PageImpl<>(
                USER_MAP.values()
                        .stream()
                        .filter(user -> login == null || user.getLogin().startsWith(login))
                        .filter(user -> role == null || user.getRole().equals(Role.valueOf(role)))
                        .map(user -> new User(
                                user.getId(),
                                user.getLogin(),
                                user.getRole(),
                                user.getLock(),
                                user.getCredentials(),
                                user.getSession()
                        ))
                        .skip(pageable.getOffset())
                        .limit(pageable.getPageSize())
                        .collect(Collectors.toList()),
                pageable,
                USER_MAP.size()
        );
    }
}
