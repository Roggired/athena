package ru.yofik.athena.auth.domain.auth.service.password;

public interface PasswordService {
    String hash(String password);
}
