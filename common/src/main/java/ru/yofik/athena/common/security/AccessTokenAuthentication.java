package ru.yofik.athena.common.security;


import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public class AccessTokenAuthentication extends AbstractAuthenticationToken {
    private final long userId;

    public AccessTokenAuthentication(
            long userId,
            Set<String> authorities
    ) {
        super(authorities.stream()
                .map((s) -> (GrantedAuthority) () -> s)
                .collect(Collectors.toSet()));
        this.userId = userId;
        this.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return "";
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }
}
