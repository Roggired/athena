package ru.yofik.messenger.service.auth;

import ru.yofik.messenger.model.domain.user.Access;

public interface JwtTokenFactory {
    Access generate(long userId);
}
