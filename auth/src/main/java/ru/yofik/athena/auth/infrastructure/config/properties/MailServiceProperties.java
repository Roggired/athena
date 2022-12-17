package ru.yofik.athena.auth.infrastructure.config.properties;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@ConstructorBinding
@ConfigurationProperties(prefix = "mail-service")
public class MailServiceProperties {
    public final String fromEmail;
    public final String invitationLink;
    public final String resetPasswordUrl;
}
