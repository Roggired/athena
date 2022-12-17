package ru.yofik.athena.auth.domain.auth.service.jwt;

import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.yofik.athena.auth.domain.auth.model.JwtClaim;
import ru.yofik.athena.auth.domain.auth.model.JwtPurpose;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.utils.Pair;
import ru.yofik.athena.auth.utils.TimeUtils;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class JwtChecker {
    private final ECKey ecKey;

    @SneakyThrows
    public Map<JwtClaim, ?> checkSignAndParseClaims(String token) {
        SignedJWT signedJwt;
        try {
            signedJwt = SignedJWT.parse(token);
        } catch (ParseException e) {
            throw new InvalidTokenException();
        }

        var verifier = new ECDSAVerifier(ecKey);
        if (!signedJwt.verify(verifier)) {
            throw new InvalidTokenException();
        }

        verifyGeneralClaims(signedJwt);

        return Arrays.stream(JwtClaim.values())
                .map(claim -> {
                    try {
                        if (JwtClaim.USER_ID == claim) {
                            return Pair.of(claim, signedJwt.getJWTClaimsSet().getLongClaim(claim.getValue()));
                        }

                        if (JwtClaim.USER_ROLE == claim) {
                            if (signedJwt.getJWTClaimsSet().getStringClaim(claim.getValue()) == null) {
                                return Pair.of(claim, null);
                            }
                            return Pair.of(claim, Role.valueOf(signedJwt.getJWTClaimsSet().getStringClaim(claim.getValue())));
                        }

                        if (JwtClaim.USER_SESSION_ID == claim) {
                            return Pair.of(claim, signedJwt.getJWTClaimsSet().getStringClaim(claim.getValue()));
                        }

                        if (JwtClaim.TOKEN_TYPE == claim) {
                            return Pair.of(claim, JwtPurpose.valueOf(signedJwt.getJWTClaimsSet().getStringClaim(claim.getValue())));
                        }

                        return Pair.of(claim, null);
                    } catch (ParseException e) {
                        throw new InvalidTokenException();
                    }
                })
                .filter(pair -> pair.second() != null)
                .collect(Collectors.toMap(Pair::first, Pair::second));
    }

    @SneakyThrows
    private void verifyGeneralClaims(SignedJWT signedJWT) {
        var expiresAt = (Long) signedJWT.getPayload().toJSONObject().get(JwtClaim.EXPIRES_AT.getValue());
        var type = signedJWT.getJWTClaimsSet().getStringClaim(JwtClaim.TOKEN_TYPE.getValue());

        if (expiresAt == null || type == null) {
            throw new InvalidTokenException();
        }

        if (TimeUtils.UTCTime(expiresAt).isBefore(TimeUtils.nowUTC())) {
            throw new InvalidTokenException();
        }

        Arrays.stream(JwtPurpose.values())
                .filter(purpose -> purpose.name().equals(type))
                .findAny()
                .orElseThrow(InvalidTokenException::new);
    }
}
