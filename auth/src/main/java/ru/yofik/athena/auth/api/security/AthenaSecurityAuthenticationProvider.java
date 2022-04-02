package ru.yofik.athena.auth.api.security;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import ru.yofik.athena.auth.api.exception.ResourceNotFoundException;
import ru.yofik.athena.auth.context.client.dto.ClientJpaDto;
import ru.yofik.athena.auth.context.client.service.ClientService;
import ru.yofik.athena.auth.context.client.view.ClientView;
import ru.yofik.athena.auth.infrastructure.SpringContext;
import ru.yofik.athena.auth.infrastructure.security.InvalidTokenException;
import ru.yofik.athena.auth.infrastructure.security.Token;
import ru.yofik.athena.auth.infrastructure.security.TokenVerifier;

import java.util.HashSet;
import java.util.stream.Collectors;

@Log4j2
public final class AthenaSecurityAuthenticationProvider implements AuthenticationProvider {
    private final TokenVerifier<ClientJpaDto> tokenVerifier = SpringContext.getBean(TokenVerifier.class);
    private final ClientService clientService = SpringContext.getBean(ClientService.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        var jweAuthenticationToken = (JweAuthenticationToken) authentication;
        var tokenData = (char[]) jweAuthenticationToken.getCredentials();

        ClientJpaDto clientJpaDto;
        try {
            clientJpaDto = tokenVerifier.verify(new Token(tokenData, Token.Type.JWE), ClientJpaDto.class);
        } catch (InvalidTokenException e) {
            return abort(jweAuthenticationToken);
        }

        ClientView client;
        try {
            client = clientService.getClient(clientJpaDto.getId());
        } catch (ResourceNotFoundException e) {
            return abort(jweAuthenticationToken);
        }

        if (!client.isActive()) {
            return abort(jweAuthenticationToken);
        }

        jweAuthenticationToken.setAuthenticated(true);
        jweAuthenticationToken.eraseToken();
        jweAuthenticationToken.setPrincipal(clientJpaDto::getName);
        jweAuthenticationToken.addAll(
                clientJpaDto.getClientPermissions()
                        .stream()
                        .map(clientPermission -> (GrantedAuthority) clientPermission::name)
                        .collect(Collectors.toCollection(HashSet::new))
        );

        return jweAuthenticationToken;
    }

    private JweAuthenticationToken abort(JweAuthenticationToken jweAuthenticationToken) {
        jweAuthenticationToken.setAuthenticated(false);
        jweAuthenticationToken.eraseToken();
        return jweAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication == JweAuthenticationToken.class;
    }
}
