package ru.yofik.athena.auth.service.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yofik.athena.auth.domain.auth.service.AccessService;
import ru.yofik.athena.auth.domain.auth.service.AccessServiceImpl;
import ru.yofik.athena.auth.domain.user.model.Credentials;
import ru.yofik.athena.auth.domain.user.model.Lock;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AccessServiceImplTest {
    private AccessService accessService;
    private UserService userService;

    @BeforeEach
    public void setup() {
        userService = Mockito.mock(UserService.class);
        accessService = new AccessServiceImpl(userService);
    }


    @Test
    public void authServiceCorrectlyLoginAdmin() {
        var login = "qwerty";
        var password = "12345678";

        when(userService.getUser(eq(login))).thenReturn(
                new User(
                        1,
                        login,
                        Role.ADMIN,
                        new Lock(2, false, ""),
                        Credentials.newAdminCredentials(password),
                        null
                )
        );

        var actual = accessService.loginAdmin(login, password);
        assertEquals(1L, actual.getUserId());
        assertEquals(Role.ADMIN, actual.getRole());
        assertFalse(actual.getSessionId().isBlank());
    }

    @Test
    public void authServiceThrowsOnLoginNotExist() {
        var login = "qwerty";
        var password = "12345678";

        when(userService.getUser(eq(login))).thenThrow(new NotFoundException());

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> accessService.loginAdmin(login, password)
        );
    }

    @Test
    public void authServiceThrowsOnLoginNotAdmin() {
        var login = "qwerty";
        var password = "12345678";

        when(userService.getUser(eq(login))).thenReturn(
                new User(
                        1,
                        login,
                        Role.USER,
                        new Lock(2, false, ""),
                        Credentials.newAdminCredentials(password),
                        null
                )
        );

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> accessService.loginAdmin(login, password)
        );
    }

    @Test
    public void authServiceThrowsOnLoginWrongPassword() {
        var login = "qwerty";
        var password = "12345678";

        when(userService.getUser(eq(login))).thenReturn(
                new User(
                        1,
                        login,
                        Role.ADMIN,
                        new Lock(2, false, ""),
                        Credentials.newAdminCredentials(password + "0"),
                        null
                )
        );

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> accessService.loginAdmin(login, password)
        );
    }
}
