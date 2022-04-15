package ru.yofik.athena.auth.context.user.factory;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Profile("prod")
public class ProdCodeGenerator implements CodeGenerator {
    @Override
    public String generate() {
        return UUID.randomUUID().toString();
    }
}
