package ru.yofik.messenger.api.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.yofik.messenger.model.user.Access;
import ru.yofik.messenger.service.auth.AuthServiceImpl;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthMessengerFilter extends OncePerRequestFilter {
    private final AuthServiceImpl authServiceImpl;


    public AuthMessengerFilter(AuthServiceImpl authServiceImpl) {
        this.authServiceImpl = authServiceImpl;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String token = header.split(" ")[1].trim();
        var optionalUser = authServiceImpl.identifyUser(new Access(token));

        if (optionalUser.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        var user = optionalUser.get();

        var authentication = new UsernamePasswordAuthenticationToken(
                user,
                token,
                user.getAuthorities()
        );

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request)
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
