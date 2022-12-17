package ru.yofik.athena.auth.domain.auth.service.password;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

@Component
public class SHA512PasswordService implements PasswordService {
    @Override
    @SneakyThrows
    public String hash(String password) {
        return Base64.getEncoder()
                .encodeToString(
                        MessageDigest.getInstance("SHA-512")
                                .digest(password.getBytes(StandardCharsets.UTF_8))
                );
    }
}
