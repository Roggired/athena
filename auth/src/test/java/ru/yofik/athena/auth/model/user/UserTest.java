package ru.yofik.athena.auth.model.user;

import org.junit.jupiter.api.Test;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.common.utils.TimeUtils;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    @Test
    void userCreatesCorrectly() {
        var user = User.newUser(
                "qwerty@qwerty.com",
                "qwerty",
                "1234"
        );

        assertEquals(
                0,
                user.getId()
        );
        assertEquals(
                "qwerty",
                user.getLogin()
        );
        assertEquals(
                Role.USER,
                user.getRole()
        );

        assertNotNull(user.getLock());
        assertEquals("", user.getLockReason());
        assertFalse(user.isLocked());

        assertNotNull(user.getCredentials());
        assertTrue(TimeUtils.nowUTC().plusHours(24).isAfter(user.getCredentialsExpirationDate()));
        assertFalse(user.isCredentialsExpired());

        assertNotNull(user.getSession());
        assertEquals(TimeUtils.infinity(), user.getLastLoginDate());
    }

    @Test
    void challengeReturnsCorrectly() {
        var user = User.newUser(
                "qwerty",
                "123",
                "1234"
        );
        assertTrue(user.challengeCredentials("1234"));
        assertFalse(user.challengeCredentials("567"));
    }

    @Test
    void adminCreatesCorrectly() {
        var admin = User.newAdmin(
                "qwerty@qwerty.com",
                "qwerty",
                "12345678",
                false
        );

        assertEquals(
                0,
                admin.getId()
        );
        assertEquals(
                "qwerty",
                admin.getLogin()
        );
        assertEquals(
                Role.ADMIN,
                admin.getRole()
        );

        assertNotNull(admin.getLock());
        assertEquals("", admin.getLockReason());
        assertFalse(admin.isLocked());

        assertNotNull(admin.getCredentials());
        assertEquals(TimeUtils.infinity(), admin.getCredentialsExpirationDate());
        assertFalse(admin.isCredentialsExpired());

        assertNotNull(admin.getSession());
        assertEquals(TimeUtils.infinity(), admin.getLastLoginDate());
    }

    @Test
    void adminCreatesThrowsOnTooShortPassword() {
        assertThrows(
                InvalidDataException.class,
                () -> User.newAdmin(
                        "qwerty",
                        "12345",
                        "1234",
                        false
                )
        );
    }
}
