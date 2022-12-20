package ru.yofik.athena.common.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;

public class AccessTokenAuthenticationOAuth2Converter implements Converter<Jwt, AccessTokenAuthentication> {
    @Override
    public AccessTokenAuthentication convert(Jwt source) {
        Long userId = source.getClaim("userId");
        String role = source.getClaim("role");

        if (userId == null || role == null) {
            throw new IllegalArgumentException("Can't find all required claims in accessToken. Is userId present: " + (userId != null) + ". Is role present: " + (role != null));
        }

        return new AccessTokenAuthentication(
                source.getTokenValue(),
                userId,
                Set.of(role)
        );
    }
}
