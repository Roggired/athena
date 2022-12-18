package ru.yofik.athena.auth.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import ru.yofik.athena.auth.infrastructure.config.properties.MailSenderProperties;

import java.util.Properties;

@Configuration
@Profile({"stage"})
public class MailSenderConfiguration {
    @Bean
    public JavaMailSender javaMailSender(MailSenderProperties mailSenderProperties) {
        var properties = new Properties();
        properties.setProperty("mail.transport.protocol", "smtp");
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.starttls.enable", "true");
        properties.setProperty("mail.debug", "true");

        var javaMailSender = new JavaMailSenderImpl();
        javaMailSender.setHost(mailSenderProperties.getHost());
        javaMailSender.setPort(mailSenderProperties.getPort());
        javaMailSender.setUsername(mailSenderProperties.getUsername());
        javaMailSender.setPassword(mailSenderProperties.getPassword());
        javaMailSender.setJavaMailProperties(properties);

        return javaMailSender;
    }
}
