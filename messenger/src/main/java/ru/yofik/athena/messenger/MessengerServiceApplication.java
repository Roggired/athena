package ru.yofik.athena.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
@EnableConfigurationProperties
@ConfigurationPropertiesScan(basePackages = "ru.yofik.athena.messenger.infrastructure.config.properties")
public class MessengerServiceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MessengerServiceApplication.class, args);
    }
}
