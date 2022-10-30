package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.auth.utils.TimeUtils;

import javax.persistence.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "credentials")
public class Credentials {
    private static final String HASH_ALGORITHM = "SHA-512";
    private static final int PASSWORD_MIN_LENGTH = 8;
    // hours
    private static final long TIME_TO_LIVE = 24;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String value;
    @Column
    private LocalDateTime expirationDate;


    public static Credentials newUserCredentials(String value) {
        try {
            return new Credentials(
                    0,
                    Base64.getEncoder().encodeToString(
                            MessageDigest.getInstance(HASH_ALGORITHM)
                                    .digest(value.getBytes(StandardCharsets.UTF_8))
                    ),
                    TimeUtils.now().plusHours(TIME_TO_LIVE)
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static Credentials newAdminCredentials(String value) {
        if (value.length() < PASSWORD_MIN_LENGTH) {
            throw new InvalidDataException("Password is too short");
        }

        try {
            return new Credentials(
                    0,
                    Base64.getEncoder().encodeToString(
                            MessageDigest.getInstance(HASH_ALGORITHM)
                                    .digest(value.getBytes(StandardCharsets.UTF_8))
                    ),
                    TimeUtils.infinity()
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isExpired(LocalDateTime currentTime) {
        return currentTime.isAfter(expirationDate);
    }

    public boolean challenge(String userCredentials) {
        try {
            var algo = MessageDigest.getInstance(HASH_ALGORITHM);
            var userBytes = algo.digest(userCredentials.getBytes(StandardCharsets.UTF_8));
            return Arrays.equals(
                    Base64.getDecoder().decode(value),
                    userBytes
            );
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
