package ru.yofik.athena.auth.domain.auth.service.code;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.infrastructure.config.properties.ApacheCommonsCodeGeneratorProperties;

@RequiredArgsConstructor
@Service
@Profile({"prod"})
public class ApacheCommonsCodeGenerator implements CodeGenerator {
    private final ApacheCommonsCodeGeneratorProperties apacheCommonsCodeGeneratorProperties;


    @Override
    public String generateShort() {
        return RandomStringUtils.randomAlphanumeric(apacheCommonsCodeGeneratorProperties.shortCodeLength).toUpperCase();
    }

    @Override
    public String generateLong() {
        return RandomStringUtils.randomAlphanumeric(apacheCommonsCodeGeneratorProperties.longCodeLength).toUpperCase();
    }
}
