package ru.yofik.athena.auth.context.user.factory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.Random;

@Component
@Profile("dev")
public class DevCodeGenerator implements CodeGenerator {
    @Override
    public String generate() {
        Random random = new SecureRandom();
        int code = random.nextInt(900) + 100;
        return code + "";
    }
}
