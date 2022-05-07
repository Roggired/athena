package ru.yofik.athena.messenger.api.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.client.HttpClientErrorException;
import ru.yofik.athena.messenger.api.exception.ForbiddenException;
import ru.yofik.athena.messenger.context.user.integration.UserApi;
import ru.yofik.athena.messenger.context.user.model.User;
import ru.yofik.athena.messenger.context.user.service.UserService;
import ru.yofik.athena.messenger.context.user.view.UserView;
import ru.yofik.athena.messenger.infrastructure.SpringContext;

import java.util.Collections;

@Log4j2
public final class AthenaSecurityAuthenticationProvider implements AuthenticationProvider {
    private final UserService userService = SpringContext.getBean(UserService.class);
    private final UserApi userApi = SpringContext.getBean(UserApi.class);


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jweAuthenticationToken = (JweAuthenticationToken) authentication;

        var clientTokenData = (char[]) jweAuthenticationToken.getCredentials();
        try {
            userApi.iAmTeapot(clientTokenData);
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
            jweAuthenticationToken.eraseToken();
            return jweAuthenticationToken;
        }

        var deviceId = jweAuthenticationToken.getDeviceId();
        UserView userView;
        try {
            userView = userService.authorizeUser(new String(accessTokenData), deviceId);
        } catch (HttpClientErrorException.Unauthorized |
                HttpClientErrorException.Forbidden |
                ru.yofik.athena.messenger.api.exception.AuthenticationException |
                ForbiddenException e) {
            log.warn(() -> "", e);
            return abort(jweAuthenticationToken);
        }

        var user = new User(userView.id, userView.name, userView.login);
        jweAuthenticationToken.setAuthenticated(true);
//        jweAuthenticationToken.eraseToken();
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
