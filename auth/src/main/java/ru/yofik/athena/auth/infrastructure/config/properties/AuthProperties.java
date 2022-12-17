package ru.yofik.athena.auth.infrastructure.config.properties;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.Duration;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "auth")
public class AuthProperties {
    public final Duration accessTokenExpirationDuration;
    public final Duration refreshTokenExpirationDuration;
    public final Duration changePasswordCodeDuration;
    public final String keysFileName;
    public final String jwksUrl;
    public final String kid;
}
