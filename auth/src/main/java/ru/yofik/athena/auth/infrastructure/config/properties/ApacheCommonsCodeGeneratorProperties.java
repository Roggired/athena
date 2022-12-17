package ru.yofik.athena.auth.infrastructure.config.properties;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "code.generator")
@AllArgsConstructor
public class ApacheCommonsCodeGeneratorProperties {
    public final int shortCodeLength;
    public final int longCodeLength;
}
