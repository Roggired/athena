package ru.yofik.athena.auth.infrastructure.security.jwe;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
import org.jose4j.jwa.AlgorithmConstraints;
import org.jose4j.jwe.ContentEncryptionAlgorithmIdentifiers;
import org.jose4j.jwe.JsonWebEncryption;
import org.jose4j.jwe.KeyManagementAlgorithmIdentifiers;
import org.jose4j.keys.AesKey;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.infrastructure.security.InvalidTokenException;
import ru.yofik.athena.auth.infrastructure.security.Token;
import ru.yofik.athena.auth.infrastructure.security.TokenVerifier;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.ZonedDateTime;

@Component
@Log4j2
public class JweTokenVerifier<T> implements TokenVerifier<T> {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(ZonedDateTime.class, (JsonDeserializer<ZonedDateTime>) (json, typeOfT, context) -> ZonedDateTime.parse(json.getAsString()))
            .create();

    @Value("${yofik.security.jwe-key}")
    private String stringKey;

    private Key key;

    @PostConstruct
    public void init () {
        key = new AesKey(stringKey.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public T verify(Token token, Class<T> payloadType) throws InvalidTokenException {
        try {
            var jsonPayload = verifyAndParseJwe(token);
            return GSON.fromJson(jsonPayload, payloadType);
        } catch (JoseException e) {
            log.warn(() -> "Invalid token");
            throw new InvalidTokenException();
        }
    }

    private String verifyAndParseJwe(Token token) throws JoseException {
        var jwe = new JsonWebEncryption();
        jwe.setAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                KeyManagementAlgorithmIdentifiers.A128KW));
        jwe.setContentEncryptionAlgorithmConstraints(new AlgorithmConstraints(AlgorithmConstraints.ConstraintType.PERMIT,
                ContentEncryptionAlgorithmIdentifiers.AES_128_CBC_HMAC_SHA_256));
        jwe.setKey(key);
        jwe.setCompactSerialization(new String(token.getData()));
        return jwe.getPayload();
    }
}
