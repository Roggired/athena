package ru.yofik.athena.auth.utils;

import org.springframework.security.core.context.SecurityContextHolder;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;

public class SecurityUtils {
    public static InternalAccess getCurrentInternalAccess() {
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();
        if (authentication == null || authentication.getClass() != InternalAccess.class) {
            throw new IllegalStateException("SecurityUtils#getCurrentInternalAccess can be used only if request is authenticated");
        }

        return (InternalAccess) authentication;
    }
}
