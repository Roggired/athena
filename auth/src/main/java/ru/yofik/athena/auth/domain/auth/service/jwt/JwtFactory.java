package ru.yofik.athena.auth.domain.auth.service.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.jwk.ECKey;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.model.AccessJwtPayload;
import ru.yofik.athena.auth.domain.auth.model.RefreshJwtPayload;
import ru.yofik.athena.auth.infrastructure.config.properties.AuthProperties;
import ru.yofik.athena.auth.utils.Pair;

import java.net.URI;

@RequiredArgsConstructor
public class JwtFactory {
    private final ECKey ecKey;
    private final AuthProperties authProperties;

    public String getPublicJWKAsJson() {
        return ecKey.toJSONString();
    }

    @SneakyThrows
    public Pair<String, String> generateTokens(InternalAccess internalAccess) {
        var signer = new ECDSASigner(ecKey);
        var accessTokenJWS = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES256)
                        .keyID(ecKey.getKeyID())
                        .type(JOSEObjectType.JWT)
                        .jwkURL(URI.create(authProperties.jwksUrl))
                        .build(),
                new Payload(
                        new ObjectMapper()
                                .writeValueAsString(
                                        new AccessJwtPayload(
                                                internalAccess.getUserId(),
                                                internalAccess.getSessionId(),
                                                internalAccess.getRole().name(),
                                                System.currentTimeMillis() + authProperties.accessTokenExpirationDuration.toMillis(),
                                                System.currentTimeMillis()
                                        )
                                )
                )
        );

        var refreshTokenJWS = new JWSObject(
                new JWSHeader.Builder(JWSAlgorithm.ES256)
                        .keyID(ecKey.getKeyID())
                        .type(JOSEObjectType.JWT)
                        .jwkURL(URI.create(authProperties.jwksUrl))
                        .build(),
                new Payload(
                        new ObjectMapper()
                                .writeValueAsString(
                                        new RefreshJwtPayload(
                                                internalAccess.getUserId(),
                                                internalAccess.getSessionId(),
                                                System.currentTimeMillis() + authProperties.accessTokenExpirationDuration.toMillis(),
                                                System.currentTimeMillis()
                                        )
                                )
                )
        );

        accessTokenJWS.sign(signer);
        refreshTokenJWS.sign(signer);
        return Pair.of(accessTokenJWS.serialize(), refreshTokenJWS.serialize());
    }
}
