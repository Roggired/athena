package ru.yofik.messenger.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@Slf4j
public class Auth0JwtTokenParser implements JwtTokenParser {
    @Value("${auth.hmac256-key}")
    private String key;


    @Override
    public boolean validate(String token) {
        return parse(token).isPresent();
    }

    @Override
    public int getUserId(String token) {
        return parse(token)
                .orElseThrow(JwtTokenValidationException::new)
                .getClaim("userId")
                .asInt();
    }

    private Optional<DecodedJWT> parse(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(key);
            return Optional.of(JWT.require(algorithm)
                    .withIssuer("yofik-messanger")
                    .withClaimPresence("userId")
                    .withSubject("accessToken")
                    .build()
                    .verify(token));
        } catch (JWTVerificationException e) {
            log.warn("Wrong token: " + token + " " + e.getMessage());
            return Optional.empty();
        }
    }
}
