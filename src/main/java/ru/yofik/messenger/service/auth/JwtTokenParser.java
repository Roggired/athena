package ru.yofik.messenger.service.auth;

public interface JwtTokenParser {
    boolean validate(String token);
    int getUserId(String token);
}
