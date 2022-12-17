package ru.yofik.athena.auth.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.model.JwtClaim;
import ru.yofik.athena.auth.domain.auth.model.JwtPurpose;
import ru.yofik.athena.auth.domain.auth.service.jwt.InvalidTokenException;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtChecker;
import ru.yofik.athena.auth.domain.user.model.Role;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class TokenService {
    private final JwtChecker jwtChecker;


    public InternalAccess getAccessFromRefreshToken(String refreshToken) {
        var refreshTokenClaims = jwtChecker.checkSignAndParseClaims(refreshToken);
        validateRefreshTokenClaims(refreshTokenClaims);

        return new InternalAccess(
                (Long) refreshTokenClaims.get(JwtClaim.USER_ID),
                Role.USER,
                (String) refreshTokenClaims.get(JwtClaim.USER_SESSION_ID)
        );
    }

    public InternalAccess getAccessFromAccessToken(String accessToken) {
        var accessTokenClaims = jwtChecker.checkSignAndParseClaims(accessToken);
        validateAccessTokenClaims(accessTokenClaims);

        return new InternalAccess(
                (Long) accessTokenClaims.get(JwtClaim.USER_ID),
                (Role) accessTokenClaims.get(JwtClaim.USER_ROLE),
                (String) accessTokenClaims.get(JwtClaim.USER_SESSION_ID)
        );
    }

    private void validateRefreshTokenClaims(Map<JwtClaim, ?> claims) {
        if (!claims.containsKey(JwtClaim.USER_ID) || !claims.containsKey(JwtClaim.TOKEN_TYPE)) {
            throw new InvalidTokenException();
        }

        if (claims.get(JwtClaim.TOKEN_TYPE) != JwtPurpose.REFRESH) {
            throw new InvalidTokenException();
        }
    }

    private void validateAccessTokenClaims(Map<JwtClaim, ?> claims) {
        if (!claims.containsKey(JwtClaim.USER_ID) || !claims.containsKey(JwtClaim.USER_ROLE)
            || !claims.containsKey(JwtClaim.USER_SESSION_ID) || !claims.containsKey(JwtClaim.TOKEN_TYPE)) {
            throw new InvalidTokenException();
        }

        if (claims.get(JwtClaim.TOKEN_TYPE) != JwtPurpose.ACCESS) {
            throw new InvalidTokenException();
        }
    }
}
