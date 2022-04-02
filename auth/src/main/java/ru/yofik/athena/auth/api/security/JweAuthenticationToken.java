package ru.yofik.athena.auth.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class JweAuthenticationToken implements Authentication {
    private boolean authenticated;
    private final char[] token;
    private final Set<GrantedAuthority> grantedAuthorities;
    private Principal principal;


    public JweAuthenticationToken(char[] token) {
        this.token = token;
        this.grantedAuthorities = new HashSet<>();
    }


    public void addAll(Collection<? extends GrantedAuthority> grantedAuthorities) {
        this.grantedAuthorities.addAll(grantedAuthorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    public void eraseToken() {
        Arrays.fill(token, '0');
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getDetails() {
        return "";
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.authenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return principal == null ? "" : principal.getName();
    }
}
