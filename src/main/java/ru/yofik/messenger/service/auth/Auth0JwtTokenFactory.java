package ru.yofik.messenger.service.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yofik.messenger.model.domain.user.Access;

@Component
public class Auth0JwtTokenFactory implements JwtTokenFactory {
    @Value("${auth.hmac256-key}")
    private String key;


    @Override
    public Access generate(long userId) {
        var algorithm = Algorithm.HMAC256(key);
        return new Access(
                JWT.create()
                        .withIssuer("yofik-messanger")
                        .withSubject("accessToken")
                        .withClaim("userId", userId)
                        .sign(algorithm)
        );
    }
}
