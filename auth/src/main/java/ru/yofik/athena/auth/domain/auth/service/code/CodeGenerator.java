package ru.yofik.athena.auth.domain.auth.service.code;

public interface CodeGenerator {
    String generateShort();
    String generateLong();
}
