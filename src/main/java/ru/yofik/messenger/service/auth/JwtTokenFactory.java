package ru.yofik.messenger.service.auth;

import ru.yofik.messenger.model.user.Access;

public interface JwtTokenFactory {
    Access generate(long userId);
}
