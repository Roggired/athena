package ru.yofik.athena.messenger.context.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.user.model.User;

import javax.annotation.PostConstruct;

@Service
@Log4j2
public abstract class AbstractService {
    @Value("${yofik.security.client-token}")
    private String clientTokenString;
    protected char[] clientToken;


    @PostConstruct
    public void init() {
        clientToken = clientTokenString.toCharArray();
        clientTokenString = "";
    }


    protected User getCurrentUser() {
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();

        if (authentication.getDetails() == null) {
            log.fatal("Abstract Service cannot provide user. Authentication details is null");
            throw new RuntimeException();
        }

        return (User) authentication.getDetails();
    }
}
