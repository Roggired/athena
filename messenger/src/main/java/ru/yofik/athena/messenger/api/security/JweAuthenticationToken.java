package ru.yofik.athena.messenger.api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import ru.yofik.athena.messenger.context.user.model.User;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public final class JweAuthenticationToken implements Authentication {
    private boolean authenticated;
    private final char[] clientToken;
    private final char[] accessToken;
    private final Set<GrantedAuthority> grantedAuthorities;
    private User user;


    public JweAuthenticationToken(char[] clientToken, char[] accessToken) {
        this.clientToken = clientToken;
        this.accessToken = accessToken;
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
        Arrays.fill(clientToken, '0');
        Arrays.fill(accessToken, '0');
    }

    @Override
    public Object getCredentials() {
        return clientToken;
    }

    public char[] getAccessToken() {
        return accessToken;
    }

    @Override
    public Object getDetails() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public Object getPrincipal() {
        if (user == null) {
            return "Messenger Principal";
        }

        return user.getName();
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
        return user == null ? "" : user.getName();
    }
}
