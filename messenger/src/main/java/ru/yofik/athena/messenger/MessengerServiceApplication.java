package ru.yofik.athena.messenger;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

@SpringBootApplication
public class MessengerServiceApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(MessengerServiceApplication.class, args);
    }
}
