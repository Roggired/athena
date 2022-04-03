package ru.yofik.athena.admin.api.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import ru.yofik.athena.admin.context.client.model.AdminKeyStorage;
import ru.yofik.athena.admin.context.client.service.ClientService;
import ru.yofik.athena.admin.infrastructure.SpringContext;

@Log4j2
public final class AthenaAuthenticationProvider implements AuthenticationProvider {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var athenaAuthentication = (AthenaCookieAuthentication) authentication;
        var clientService  = SpringContext.getBean(ClientService.class);
        var adminKeyStorage = SpringContext.getBean(AdminKeyStorage.class);
        var token = adminKeyStorage.get((String) athenaAuthentication.getCredentials());

        if (token == null || token.length == 0) {
            athenaAuthentication.setAuthenticated(false);
            log.warn("No session found");
            return athenaAuthentication;
        }

        try {
            clientService.iAmTeapot(token);
            authentication.setAuthenticated(true);
            ((AthenaCookieAuthentication) authentication).add(() -> "ADMIN");
            ((AthenaCookieAuthentication) authentication).setPrincipal(() -> "Athena Admin Password");
        } catch (Throwable t) {
            log.warn(() -> "Athena Auth responses unauthenticated", t);
            authentication.setAuthenticated(false);
        }

        return athenaAuthentication;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == AthenaCookieAuthentication.class;
    }
}
