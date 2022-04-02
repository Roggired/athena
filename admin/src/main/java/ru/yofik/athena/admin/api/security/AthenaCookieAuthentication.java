package ru.yofik.athena.admin.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.security.Principal;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class AthenaCookieAuthentication implements Authentication {
    private boolean authenticated;
    private final String athenaSession;
    private final Set<GrantedAuthority> grantedAuthorities;
    private Principal principal;


    public AthenaCookieAuthentication(String athenaSession) {
        this.athenaSession = athenaSession;
        this.grantedAuthorities = new HashSet<>();
    }


    public void add(GrantedAuthority grantedAuthority) {
        this.grantedAuthorities.add(grantedAuthority);
    }

    public void setPrincipal(Principal principal) {
        this.principal = principal;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public Object getCredentials() {
        return athenaSession;
    }

    @Override
    public Object getDetails() {
        return principal;
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
        return principal.getName();
    }
}
