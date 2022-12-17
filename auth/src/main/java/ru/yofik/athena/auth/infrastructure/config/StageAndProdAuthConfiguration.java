package ru.yofik.athena.auth.infrastructure.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.ECKey;
import com.nimbusds.jose.jwk.KeyUse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtChecker;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtFactory;
import ru.yofik.athena.auth.infrastructure.config.properties.AuthProperties;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Configuration
@Profile(value = {"stage", "prod"})
public class StageAndProdAuthConfiguration {
    private ECKey ecKey(AuthProperties authProperties) {
        var keyFilePath = Paths.get(authProperties.keysFileName);
        if (!Files.exists(keyFilePath)) throw new ConfigurationException("Keys file path: " + keyFilePath + " doesn't exist");

        ECKey loadedECKey;
        try {
            var pemKeysText = Files.readString(keyFilePath);
            loadedECKey = ECKey.parseFromPEMEncodedObjects(pemKeysText).toECKey();
        } catch (IOException e) {
            throw new ConfigurationException("Can't read keys file: " + keyFilePath);
        } catch (JOSEException e) {
            throw new ConfigurationException("Can't parse text from keys file: " + keyFilePath);
        }

        try {
            return new ECKey.Builder(loadedECKey.getCurve(), loadedECKey.toECPublicKey())
                    .keyID(authProperties.kid)
                    .keyUse(KeyUse.SIGNATURE)
                    .d(loadedECKey.getD())
                    .build();
        } catch (JOSEException e) {
            throw new ConfigurationException("Can't create ECKey after loading: " + keyFilePath);
        }
    }

    @Bean
    public JwtFactory jwtFactory(AuthProperties authProperties) {
        return new JwtFactory(ecKey(authProperties), authProperties);
    }

    @Bean
    public JwtChecker jwtChecker(AuthProperties authProperties) {
        return new JwtChecker(ecKey(authProperties));
    }
}
