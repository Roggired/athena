package ru.yofik.athena.messenger.infrastructure.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.infrastructure.security.JweAuthenticationToken;

@Component
@RequestScope
public class AthenaCredentialsProvider {
    @Value("${yofik.security.client-token}")
    private String clientTokenString;
    @Value("${yofik.messenger.user-agent}")
    private String messengerUserAgent;


    public AthenaClientCredentials provideMessengerCredentials() {
        return new AthenaClientCredentials(
                clientTokenString.toCharArray(),
                messengerUserAgent
        );
    }

    public AthenaClientCredentials provideClientCredentials() {
        var securityContext = SecurityContextHolder.getContext();
        var jweAuthenticationToken = (JweAuthenticationToken) securityContext.getAuthentication();

        return new AthenaClientCredentials(
                (char[]) jweAuthenticationToken.getCredentials(),
                jweAuthenticationToken.getDeviceId()
        );
    }

    public AthenaUserCredentials provideUserCredentials() {
        var securityContext = SecurityContextHolder.getContext();
        var jweAuthenticationToken = (JweAuthenticationToken) securityContext.getAuthentication();

        return new AthenaUserCredentials(
                (char[]) jweAuthenticationToken.getCredentials(),
                jweAuthenticationToken.getDeviceId(),
                jweAuthenticationToken.getAccessToken()
        );
    }
}
