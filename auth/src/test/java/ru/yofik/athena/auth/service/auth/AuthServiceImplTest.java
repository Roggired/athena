package ru.yofik.athena.auth.service.auth;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.yofik.athena.auth.api.rest.auth.requests.AdminSignInRequest;
import ru.yofik.athena.auth.api.rest.auth.requests.UserSignInRequest;
import ru.yofik.athena.auth.domain.auth.service.AuthService;
import ru.yofik.athena.auth.domain.auth.service.AuthServiceImpl;
import ru.yofik.athena.auth.domain.auth.service.PasswordNeedToBeChangedException;
import ru.yofik.athena.auth.domain.auth.service.TokenService;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtChecker;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtFactory;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.service.ResetPasswordCodeService;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.infrastructure.config.DevAuthConfiguration;
import ru.yofik.athena.auth.infrastructure.config.properties.AuthProperties;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;

import java.time.Duration;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceImplTest {
    private AuthService authService;
    private UserService userService;
    private AuthProperties authProperties;
    private ResetPasswordCodeService resetPasswordCodeService;
    private JwtFactory jwtFactory;
    private JwtChecker jwtChecker;

    @BeforeEach
    public void setup() {
        userService = Mockito.mock(UserService.class);
        authProperties = new AuthProperties(
                Duration.ofSeconds(300),
                Duration.ofDays(30),
                Duration.ofMinutes(30),
                "ec_keys.pem",
                "http://localhost:8081/api/v2/auth/jwks",
                "1234"
        );
        resetPasswordCodeService = Mockito.mock(ResetPasswordCodeService.class);
        jwtFactory = new DevAuthConfiguration().jwtFactory(authProperties);
        jwtChecker = new DevAuthConfiguration().jwtChecker(authProperties);
        authService = new AuthServiceImpl(
                userService,
                resetPasswordCodeService,
                authProperties,
                jwtFactory,
                new TokenService(jwtChecker)
        );
    }


    @Test
    public void authServiceCorrectlyLoginAdmin() throws PasswordNeedToBeChangedException {
        var login = "qwerty";
        var password = "12345678";
        var stubAdmin = User.newAdmin("qwerty@qwerty.com", login, password, false);
        stubAdmin.setId(1L);
        when(userService.getUser(eq(login))).thenReturn(stubAdmin);

        var adminRequest = new AdminSignInRequest();
        adminRequest.login = login;
        adminRequest.password = password;

        var actual = authService.loginAdmin(adminRequest);

        assertEquals(1L, actual.getUserId());
        assertEquals(Role.ADMIN, actual.getRole());
        assertFalse(actual.getSessionId().isBlank());
    }

    @Test
    public void authServiceThrowsOnLoginNotExist() {
        var login = "qwerty";
        var password = "12345678";

        when(userService.getUser(eq(login))).thenThrow(new NotFoundException());
        var request = new AdminSignInRequest();
        request.login = login;
        request.password = password;
        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authService.loginAdmin(request)
        );
    }

    @Test
    public void authServiceThrowsOnLoginNotAdmin() {
        var login = "qwerty";
        var password = "12345678";

        var stubUser = User.newUser("qwerty@qwerty.com", login, "1234");
        stubUser.setId(1L);
        when(userService.getUser(eq(login))).thenReturn(stubUser);

        var request = new AdminSignInRequest();
        request.login = login;
        request.password = password;

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authService.loginAdmin(request)
        );
    }

    @Test
    public void authServiceThrowsOnLoginWrongPassword() {
        var login = "qwerty";
        var password = "12345678";

        var stubAdmin = User.newAdmin("qwerty@qwerty.com", login, password + "1234", false);
        stubAdmin.setId(1L);

        when(userService.getUser(eq(login))).thenReturn(stubAdmin);

        var request = new AdminSignInRequest();
        request.login = login;
        request.password = password;

        Assertions.assertThrows(
                AuthenticationException.class,
                () -> authService.loginAdmin(request)
        );
    }

    @Test
    public void loginUserAndCheckUserAccessAndRefreshAccessCallSequence() {
        var stubUser = User.newUser("qwerty@qwerty.com", "qwerty", "1234");
        stubUser.setId(1L);

        when(userService.getUser(eq(stubUser.getId()))).thenReturn(stubUser);
        when(userService.updateUser(any())).thenReturn(stubUser);

        var loginRequest = new UserSignInRequest();
        loginRequest.userId = stubUser.getId();
        loginRequest.invitation = "1234";

        var tokens = authService.loginUser(loginRequest);
        assertFalse(tokens.accessToken.isBlank());
        assertFalse(tokens.refreshToken.isBlank());

        var internalAccess = authService.checkUserAccess(tokens.accessToken);
        assertEquals(Role.USER, internalAccess.getRole());
        assertEquals(stubUser.getId(), internalAccess.getUserId());
        assertFalse(internalAccess.getSessionId().isBlank());

        var refreshRequest = authService.refreshUserAccess(tokens.refreshToken);
        tokens = authService.refreshUserAccess(refreshRequest.refreshToken);
        assertFalse(tokens.accessToken.isBlank());
        assertFalse(tokens.refreshToken.isBlank());
    }
}
