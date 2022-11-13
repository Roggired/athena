package ru.yofik.athena.auth.domain.auth.service;

import ru.yofik.athena.auth.domain.auth.model.Access;

public interface AccessService {
    Access loginAdmin(String login, String password);
    Access checkUserAccess(String accessToken);
    Access refreshUserAccess(String refreshToken);
}
