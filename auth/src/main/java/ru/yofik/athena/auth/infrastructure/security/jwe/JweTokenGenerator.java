package ru.yofik.athena.auth.infrastructure.security.jwe;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.api.exception.UnexpectedException;
import ru.yofik.athena.auth.infrastructure.security.Token;
import ru.yofik.athena.auth.infrastructure.security.TokenGenerator;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;

@Component
@Log4j2
public class JweTokenGenerator<T> implements TokenGenerator<T> {
    private static final Gson GSON = new Gson();


    @Value("${yofik.security.jwe-key}")
    private String stringKey;

    private Key key;

    @PostConstruct
    public void init () {
        key = new AesKey(stringKey.getBytes(StandardCharsets.UTF_8));
    }


    @Override
    public Token generateToken(T t) {
        try {
            char[] data = generateJwe(GSON.toJson(t));
            return new Token(data, Token.Type.JWE);
        } catch (JoseException e) {
            log.fatal(() -> "Unexpected error", e);
            throw new UnexpectedException();
        }
    }

    private char[] generateJwe(String payload) throws JoseException {
        var jwe = new JsonWebEncryption();
        jwe.setPayload(payload);
        jwe.setAlgorithmHeaderValue(KeyManagementAlgorithmIdentifiers.A128KW);
        jwe.setEncryptionMethodHeaderParameter(ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256);
        jwe.setKey(key);
        return jwe.getCompactSerialization().toCharArray();
    }
}
