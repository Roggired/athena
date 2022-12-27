package ru.yofik.athena.auth.api.rest.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.rest.auth.requests.*;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.service.AuthService;
import ru.yofik.athena.auth.domain.auth.service.PasswordNeedToBeChangedException;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/admins/sign-in")
    public AuthV1Response adminSignIn(
            @RequestBody @Valid AdminSignInRequest request,
            HttpServletRequest servletRequest
    ) {
        InternalAccess adminAccess;
        try {
            adminAccess = authService.loginAdmin(request);
            var session = servletRequest.getSession();
            session.setAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY, adminAccess);
            return AuthV1Response.of(
                    AuthV1ResponseStatus.OPERATION_OK,
                    ""
            );
        } catch (PasswordNeedToBeChangedException e) {
            return AuthV1Response.of(
                    AuthV1ResponseStatus.NEED_TO_CHANGE_CREDENTIALS,
                    e.getAdminChangePasswordResponse()
            );
        }
    }

    @PostMapping("/admins/sign-out")
    public AuthV1Response adminSignOut(
            HttpServletRequest request
    ) {
        request.getSession().removeAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY);
        authService.logout();
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                ""
        );
    }

    @PostMapping("/admins/reset-password")
    public AuthV1Response adminsResetPassword(
            @RequestBody @Valid ChangeAdminTemporaryPasswordRequest request
    ) {
        authService.changeAdminTemporaryPassword(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                ""
        );
    }

    @PostMapping("/users/request-sign-up")
    public AuthV1Response userRequestSignUp(
            @RequestBody @Valid RequestUserRegistrationRequest request
    ) {
        authService.requestUserRegistration(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                "Ok"
        );
    }

    @PostMapping("/users/sign-in")
    public AuthV1Response userSignIn(
            @RequestBody @Valid UserSignInRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                authService.loginUser(request)
        );
    }

    @PostMapping("/users/request-sign-in")
    public AuthV1Response userRequestSignIn(
            @RequestBody @Valid RequestUserLoginRequest request
    ) {
        authService.requestUserLogin(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                "Ok"
        );
    }

    @PostMapping("/users/sign-out")
    public AuthV1Response userSignOut() {
        authService.logout();
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                ""
        );
    }

    @PostMapping("/users/access/refresh")
    public AuthV1Response userRefreshAccess(
            @RequestBody @Valid RefreshUserAccessRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                authService.refreshUserAccess(request.refreshToken)
        );
    }

    @GetMapping("/jwks")
    public String getJwks() {
        return authService.getPublicJwksAsJson();
    }
}
