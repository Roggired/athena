package ru.yofik.athena.auth.infrastructure.security;

import org.springframework.stereotype.Component;

@Component
public interface TokenGenerator<T> {
    Token generateToken(T tokenPayload);
}
