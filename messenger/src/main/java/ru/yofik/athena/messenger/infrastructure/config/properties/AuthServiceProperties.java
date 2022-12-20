package ru.yofik.athena.messenger.infrastructure.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "auth-service")
@ConstructorBinding
public class AuthServiceProperties {
    private final String baseUrl;
    private final String truststore;
    private final String truststorePassword;
}
