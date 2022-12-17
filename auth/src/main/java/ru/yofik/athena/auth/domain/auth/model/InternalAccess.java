package ru.yofik.athena.auth.domain.auth.model;

import lombok.*;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import ru.yofik.athena.auth.domain.user.model.Role;

import java.io.Serializable;
import java.util.Collections;

@Getter
@Setter
@ToString
public class InternalAccess extends AbstractAuthenticationToken implements Serializable {
    private static final long serialVersionUID = 1L;

    public static final String ACCESS_SERVLET_SESSION_KEY = "ACCESS";

    private final long userId;
    private final Role role;
    private final String sessionId;

    public InternalAccess(long userId, Role role, String sessionId) {
        super(Collections.singleton(role::name));
        setAuthenticated(true);

        this.userId = userId;
        this.role = role;
        this.sessionId = sessionId;
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
