package ru.yofik.messenger.auth.api.security;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1Response;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Log4j2
public final class YofikBearerAuthenticationFilter extends OncePerRequestFilter {
    private static final Gson GSON = new Gson();


    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        var authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || authorizationHeader.isBlank() || !authorizationHeader.startsWith("Bearer")) {
            sendUnauthenticated(response);
            return;
        }

        var tokenData = authorizationHeader.split(" ")[1].toCharArray();
        var jweAuthenticationToken = new JweAuthenticationToken(tokenData);
        var securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(jweAuthenticationToken);
        SecurityContextHolder.setContext(securityContext);

        filterChain.doFilter(request, response);
    }

    private void sendUnauthenticated(HttpServletResponse response) throws IOException {
        response.sendError(
                AuthV1ResponseStatus.UNAUTHENTICATED.getHttpStatusCode(),
                GSON.toJson(AuthV1Response.of(AuthV1ResponseStatus.UNAUTHENTICATED, "Fuch off!!!"))
        );
    }
}
