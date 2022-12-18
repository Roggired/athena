package ru.yofik.athena.auth.infrastructure.config.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConfigurationProperties(prefix = "mail-sender")
@ConstructorBinding
@RequiredArgsConstructor
public class MailSenderProperties {
    private final String host;
    private final int port;
    private final String username;
    private final String password;
}
