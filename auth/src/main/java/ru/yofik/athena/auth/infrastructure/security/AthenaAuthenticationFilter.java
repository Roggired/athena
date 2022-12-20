package ru.yofik.athena.auth.infrastructure.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.service.AuthService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class AthenaAuthenticationFilter implements Filter {
    private static final String WRONG_ACCESS_CLASS_MESSAGE = "AthenaAuthenticationFilter has found object in the " +
            "servlet session by ru.yofik.athena.auth.domain.auth.model.Access#ACCESS_SERVLET_SESSION_KEY, but the " +
            "object is not an implementation of the target class!";
    private static final String BEARER_TOKEN_AUTHORIZATION_PREFIX = "Bearer ";


    private final AuthService authService;


    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        var httpRequest = (HttpServletRequest) request;
        var session = httpRequest.getSession(false);

        if (session == null) {
            log.debug("No session found");
        } else {
            log.debug("Is session new? " + session.isNew() + " session id: " + session.getId() + " max inactive interval: " + session.getMaxInactiveInterval());
        }
        var securityContext = SecurityContextHolder.getContext();

        var accessToken = extractAccessToken(httpRequest);
        if (accessToken != null) {
            var internalAccess = authService.checkUserAccess(accessToken);
            securityContext.setAuthentication(internalAccess);
        } else if (isUserAuthenticatedAsAdmin(session)) {
            var maybeAccess = session.getAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY);
            if (maybeAccess.getClass() != InternalAccess.class) {
                throw new IllegalStateException(WRONG_ACCESS_CLASS_MESSAGE);
            }

            securityContext.setAuthentication((InternalAccess) maybeAccess);
        }

        chain.doFilter(request, response);
    }

    private boolean isUserAuthenticatedAsAdmin(HttpSession session) {
        return session != null && session.getAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY) != null;
    }

    private String extractAccessToken(HttpServletRequest request) {
        var authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (authorizationHeaderValue == null) {
            return null;
        }

        if (!authorizationHeaderValue.startsWith(BEARER_TOKEN_AUTHORIZATION_PREFIX)) {
            throw new AccessDeniedException("Wrong authorization schema");
        }

        var accessToken = authorizationHeaderValue.substring(BEARER_TOKEN_AUTHORIZATION_PREFIX.length()).trim();
        if (accessToken.isBlank()) {
            throw new AccessDeniedException("No access token");
        }

        return accessToken;
    }
}
