package ru.yofik.athena.messenger.infrastructure.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.HttpClientErrorException;
import ru.yofik.athena.messenger.api.exception.ForbiddenException;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.SpringContext;
import ru.yofik.athena.messenger.infrastructure.integration.auth.AthenaAuthApi;

import java.util.Collections;

@Log4j2
public final class AthenaSecurityAuthenticationProvider implements AuthenticationProvider {
    private final AthenaAuthApi athenaAuthApi = SpringContext.getBean(AthenaAuthApi.class);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jweAuthenticationToken = (JweAuthenticationToken) authentication;

        try {
            athenaAuthApi.iAmTeapot();
        } catch (HttpClientErrorException.Unauthorized |
                HttpClientErrorException.Forbidden |
                ru.yofik.athena.messenger.api.exception.AuthenticationException |
                ForbiddenException e) {
            log.warn(() -> "", e);
            return abort(jweAuthenticationToken);
        }

        var accessTokenData = jweAuthenticationToken.getAccessToken();
        if (accessTokenData.length == 0) {
            jweAuthenticationToken.setAuthenticated(true);
            jweAuthenticationToken.addAll(Collections.singleton(Authorities.ACTIVATE_USER));
            return jweAuthenticationToken;
        }

        User user;
        try {
            user = athenaAuthApi.authorize(new String(accessTokenData));
        } catch (HttpClientErrorException.Unauthorized |
                HttpClientErrorException.Forbidden |
                ru.yofik.athena.messenger.api.exception.AuthenticationException |
                ForbiddenException e) {
            log.warn(() -> "", e);
            return abort(jweAuthenticationToken);
        }

        jweAuthenticationToken.setAuthenticated(true);
        jweAuthenticationToken.setUser(user);
        jweAuthenticationToken.addAll(Collections.singleton(Authorities.FULL_AUTHORITY));
        return jweAuthenticationToken;
    }

    private JweAuthenticationToken abort(JweAuthenticationToken jweAuthenticationToken) {
        jweAuthenticationToken.setAuthenticated(false);
        jweAuthenticationToken.eraseToken();
        return jweAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == JweAuthenticationToken.class;
    }
}
