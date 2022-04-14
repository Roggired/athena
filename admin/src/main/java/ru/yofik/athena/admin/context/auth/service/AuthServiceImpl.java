package ru.yofik.athena.admin.context.auth.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.admin.api.security.AthenaAuthenticationFilter;
import ru.yofik.athena.admin.context.auth.integration.AuthApi;
import ru.yofik.athena.admin.context.client.model.AdminKeyStorage;
import ru.yofik.athena.admin.context.client.model.Token;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Service
@Log4j2
@RequestScope
public class AuthServiceImpl implements AuthService {
    private final AuthApi authApi;
    private final AdminKeyStorage adminKeyStorage;


    public AuthServiceImpl(AuthApi authApi, AdminKeyStorage adminKeyStorage) {
        this.authApi = authApi;
        this.adminKeyStorage = adminKeyStorage;
    }

    @Override
    public Token login(String password, HttpServletResponse servletResponse) {
        var token = authApi.login(password);
        var athenaSession = UUID.randomUUID().toString();
        adminKeyStorage.add(athenaSession, token.getToken());
        var cookie = new Cookie(AthenaAuthenticationFilter.COOKIE_NAME, athenaSession);
        cookie.setPath("/");
        cookie.setMaxAge(30*60);
        cookie.setSecure(false);
        cookie.setHttpOnly(true);
        servletResponse.addCookie(cookie);
        return token;
    }

    @Override
    public void logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            adminKeyStorage.remove((String) authentication.getCredentials());
        }
    }
}
