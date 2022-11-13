package ru.yofik.athena.auth.domain.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.domain.auth.model.Access;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;

import java.util.UUID;

@AllArgsConstructor
@Service
@Slf4j
public class AccessServiceImpl implements AccessService {
    private final UserService userService;


    @Override
    public Access loginAdmin(String login, String password) {
        User admin;
        try {
            admin = userService.getUser(login);
        } catch (NotFoundException e) {
            throw new AuthenticationException();
        }

        if (!admin.challengeCredentials(password)) {
            throw new AuthenticationException();
        }

        if (admin.getRole() != Role.ADMIN) {
            throw new AuthenticationException();
        }

        return new Access(
                admin.getId(),
                Role.ADMIN,
                UUID.randomUUID().toString()
        );
    }

    @Override
    public Access checkUserAccess(String accessToken) {
        return null;
    }

    @Override
    public Access refreshUserAccess(String refreshToken) {
        return null;
    }
}
