package ru.yofik.athena.auth.domain.auth.service.code;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Service
@Profile({"dev", "stage"})
public final class DevCodeGenerator implements CodeGenerator {
    public String generateShort() {
        return "123";
    }
    public String generateLong() {
        return "123";
    }
}