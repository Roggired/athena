package ru.yofik.messenger.auth.infrastructure.security;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public final class Token {
    private final char[] data;
    private final Type type;


    public enum Type {
        JWE
    }
}
