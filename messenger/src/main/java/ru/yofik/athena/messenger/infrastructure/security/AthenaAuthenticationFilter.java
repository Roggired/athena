package ru.yofik.athena.messenger.infrastructure.security;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
public final class AthenaAuthenticationFilter extends OncePerRequestFilter {
    private static final Gson GSON = new Gson();


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response);
            return;
        }

        var parts = authorizationHeader.split(" ");
        if (parts.length != 2 && parts.length != 3) {
            filterChain.doFilter(request, response);
            return;
        }

        var clientTokenData = parts[1].toCharArray();
        var accessTokenData = parts.length == 3 ? parts[2].toCharArray() : new char[0];
        var deviceId = request.getHeader("User-Agent");
        var jweAuthenticationToken = new JweAuthenticationToken(clientTokenData, accessTokenData, deviceId);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(jweAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }
}
