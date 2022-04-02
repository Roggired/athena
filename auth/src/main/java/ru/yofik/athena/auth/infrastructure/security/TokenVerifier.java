package ru.yofik.athena.auth.infrastructure.security;

import org.springframework.stereotype.Component;

@Component
public interface TokenVerifier<T> {
    T verify(Token token, Class<T> payloadType) throws InvalidTokenException;
}
