package ru.yofik.athena.common.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static String getAuthenticatedUserAccessToken() {
        var accessTokenAuthentication = getAccessTokenAuthenticationFromCurrentSecurityContext();
        return (String) accessTokenAuthentication.getCredentials();
    }

    public static long getAuthenticatedUserId() {
        var accessTokenAuthentication = getAccessTokenAuthenticationFromCurrentSecurityContext();
        return (Long) accessTokenAuthentication.getPrincipal();
    }

    private static AccessTokenAuthentication getAccessTokenAuthenticationFromCurrentSecurityContext() {
        var securityContext = SecurityContextHolder.getContext();
        var authentication = securityContext.getAuthentication();
        if (authentication == null || authentication.getClass() != AccessTokenAuthentication.class) {
            throw new IllegalStateException("ru.yofik.athena.common.security.SecurityUtils can be used only if Authentication object in SecurityContext is an instance of AccessTokenAuthentication");
        }

        return (AccessTokenAuthentication) authentication;
    }
}
