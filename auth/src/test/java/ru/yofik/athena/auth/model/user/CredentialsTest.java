package ru.yofik.athena.auth.model.user;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.auth.domain.user.model.Credentials;
import ru.yofik.athena.auth.utils.TimeUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;

public class CredentialsTest {
    @Test
    void userCredentialsCreatesCorrectly() throws NoSuchAlgorithmException {
        var userCredentials = Credentials.newUserCredentials("123");
        var digestedString = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512").digest("123".getBytes(StandardCharsets.UTF_8)));

        assertEquals(
                0,
                userCredentials.getId()
        );

        assertEquals(
                digestedString,
                userCredentials.getValue()
        );

        assertTrue(
                LocalDateTime.now(ZoneId.of("UTC")).plusHours(24).isAfter(userCredentials.getExpirationDate()),
                "After creations of userCredentials expirationTime must be BEFORE than now time + 24 hours in UTC"
        );
    }

    @Test
    void adminCredentialsCreatesCorrectly() throws NoSuchAlgorithmException {
        var adminCredentials = Credentials.newAdminCredentials("qwerty1234", false);
        var digestedString = Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-512").digest("qwerty1234".getBytes(StandardCharsets.UTF_8)));

        assertEquals(
                0,
                adminCredentials.getId()
        );

        assertEquals(
                digestedString,
                adminCredentials.getValue()
        );

        assertEquals(TimeUtils.infinity(), adminCredentials.getExpirationDate(), "Admin password expiration must be 'infinity'");
    }

    @Test
    void adminCredentialsThrowsOnTooShortPassword() {
        assertThrows(
                InvalidDataException.class,
                () -> Credentials.newAdminCredentials("1234", false)
        );
    }
}
