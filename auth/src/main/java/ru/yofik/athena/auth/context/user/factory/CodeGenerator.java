package ru.yofik.athena.auth.context.user.factory;

import org.springframework.stereotype.Component;

@Component
public interface CodeGenerator {
    String generate();
}
