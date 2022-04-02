package ru.yofik.athena.admin.api.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public final class AthenaAuthenticationFilter extends OncePerRequestFilter {
    public static final String COOKIE_NAME = "ATHENA_ADMIN_AUTH";


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var cookies = request.getCookies();
        if (cookies == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie cookie = null;
        for (var el : cookies) {
            if (el.getName().equals(COOKIE_NAME)) {
                cookie = el;
            }
        }

        if (cookie == null) {
            filterChain.doFilter(request, response);
            return;
        }

        var athenaSession = cookie.getValue();
        var athenaCookieAuthentication = new AthenaCookieAuthentication(athenaSession);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(athenaCookieAuthentication);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }
}