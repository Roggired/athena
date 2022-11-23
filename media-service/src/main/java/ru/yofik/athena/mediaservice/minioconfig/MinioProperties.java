package ru.yofik.athena.mediaservice.minioconfig;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "minio")
@Getter
@Setter
public class MinioProperties {
    private String url;
}

@Configuration
@PropertySource("classpath:application.yml")
@ConfigurationProperties(prefix = "minio.access")
@Getter
@Setter
class MinioAccessProperties {
    private String name;
    private String secret;
}
