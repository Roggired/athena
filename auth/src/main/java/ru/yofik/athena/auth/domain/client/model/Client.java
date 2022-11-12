package ru.yofik.athena.auth.domain.client.model;

import lombok.*;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String clientId;
    private String clientSecret;
    private String redirectUrl;


    public Client(long id, String clientId, String clientSecret, String redirectUrl) {
        this.id = id;
        this.clientId = clientId;
        this.clientSecret = clientSecret;
        this.redirectUrl = redirectUrl;
    }

    public static Client newClient(String clientId, String rawClientSecret, String redirectUrl) {
        return new Client(
                0,
                clientId,
                hashedSecret(rawClientSecret),
                redirectUrl
        );
    }

    private static String hashedSecret(String rawSecret) {
        try {
            return Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-512").digest(
                            rawSecret.getBytes(StandardCharsets.UTF_8)
                    )
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean challengeSecret(String maybeSecret) {
        return clientSecret.equals(hashedSecret(maybeSecret));
    }
}
