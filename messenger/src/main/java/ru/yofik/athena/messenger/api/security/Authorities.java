package ru.yofik.athena.messenger.api.security;

import org.springframework.security.core.GrantedAuthority;

public enum Authorities implements GrantedAuthority {
    ACTIVATE_USER,
    FULL_AUTHORITY;

    @Override
    public String getAuthority() {
        return this.toString();
    }
}
