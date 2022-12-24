package ru.yofik.athena.auth.domain.auth.service;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.rest.auth.requests.*;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.model.AdminChangePasswordResponse;
import ru.yofik.athena.auth.domain.auth.model.UserRegistrationRequest;
import ru.yofik.athena.auth.domain.auth.model.UserTokenAccess;
import ru.yofik.athena.auth.domain.auth.repository.UserRegistrationRequestRepository;
import ru.yofik.athena.auth.domain.auth.service.jwt.JwtFactory;
import ru.yofik.athena.auth.domain.auth.service.mail.MailService;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.auth.domain.user.service.ResetPasswordCodeService;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.infrastructure.config.properties.AuthProperties;
import ru.yofik.athena.auth.utils.SecurityUtils;
import ru.yofik.athena.auth.utils.TimeUtils;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;

@AllArgsConstructor
@Service
@Slf4j
public class AuthServiceImpl implements AuthService {
    private static final String WRONG_REFRESH_TOKEN_SESSION_LOCK_REASON = "Wrong sessionId in refresh token";
    private static final String WRONG_ACCESS_TOKEN_SESSION_LOCK_REASON = "Wrong sessionId in access token";
    private static final String WRONG_INVITATION_LOCK_REASON = "Wrong invitation in login request";

    private final UserService userService;
    private final ResetPasswordCodeService resetPasswordCodeService;
    private final AuthProperties authProperties;
    private final JwtFactory jwtFactory;
    private final TokenService tokenService;
    private final InvitationService invitationService;
    private final UserRegistrationRequestRepository userRegistrationRequestRepository;
    private final MailService mailService;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public InternalAccess loginAdmin(AdminSignInRequest request) throws PasswordNeedToBeChangedException {
        User admin;
        try {
            admin = userService.getUserByLogin(request.login);
        } catch (NotFoundException e) {
            throw new AuthenticationException();
        }

        if (admin.isLocked() || !admin.challengeCredentials(request.password) || admin.getRole() != Role.ADMIN) {
            throw new AuthenticationException();
        }

        if (admin.getCredentials().isTemporary()) {
            var resetPasswordCode = resetPasswordCodeService.getOrCreateForAdmin(admin);
            throw new PasswordNeedToBeChangedException(
                    new AdminChangePasswordResponse(
                            admin.getId(),
                            resetPasswordCode.getCode(),
                            authProperties.changePasswordCodeDuration.toSeconds()
                    )
            );
        }

        admin.getSession().startSession();
        userService.updateUser(admin);
        return new InternalAccess(
                admin.getId(),
                admin.getRole(),
                admin.getSession().getSessionId()
        );
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeAdminTemporaryPassword(ChangeAdminTemporaryPasswordRequest request) {
        var resetPasswordCode = resetPasswordCodeService.getByCode(request.code);
        var admin = userService.getUserById(resetPasswordCode.getAdminId());

        if (!admin.getCredentials().isTemporary()) {
            throw new IllegalStateException("Admin: " + admin.getLogin() + " " + admin.getEmail() + " has associated " +
                    "reset password code object, but his password is not temporary. Therefore, admin cannot use " +
                    "resetPassword endpoint");
        }

        admin.getCredentials().changeAdminCredentials(
                request.newPassword,
                false
        );
        userService.updateUser(admin);
        resetPasswordCodeService.markAsUsed(resetPasswordCode);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void logout() {
        var internalAccess = SecurityUtils.getCurrentInternalAccess();
        var user = userService.getUserById(internalAccess.getUserId());
        user.getSession().stopSession();
        userService.updateUser(user);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void requestUserRegistration(RequestUserRegistrationRequest request) {
        try {
            userService.getUserByEmail(request.email);
            throw new InvalidDataException("User with such email already exists");
        } catch (NotFoundException e) {
            // need to create user registration request
        }

        var userRegistrationRequest = UserRegistrationRequest.newRequest(request.email);
        userRegistrationRequestRepository.save(userRegistrationRequest);

        userService.getAdmins().forEach(admin -> {
            if (!admin.isLocked()) {
                mailService.sendNotificationAboutNewUserRegistrationRequest(admin, request.email);
            }
        });
    }

    @Override
    @Transactional
    public UserTokenAccess loginUser(UserSignInRequest request) {
        var user = userService.getUserById(request.userId);

        if (user.isLocked()) {
            throw new AuthenticationException();
        }

        if (!user.challengeCredentials(request.invitation)) {
            user.lock(WRONG_INVITATION_LOCK_REASON);
            userService.updateUser(user);
            throw new AuthenticationException();
        }

        user.getSession().startSession();
        user.getCredentials().setValue("used");
        user.getCredentials().setExpirationDate(TimeUtils.infinity());
        userService.updateUser(user);

        return grantAccess(user);
    }

    @Override
    public void requestUserLogin(RequestUserLoginRequest request) {
        var user = userService.getUserByEmail(request.email);

        if (user.isLocked()) {
            throw new NotFoundException();
        }

        invitationService.inviteUser(user);
        userService.updateUser(user);
    }

    @Override
    public InternalAccess checkUserAccess(String accessToken) {
        var access = tokenService.getAccessFromAccessToken(accessToken);
        getUserAndValidateItsSession(access, WRONG_ACCESS_TOKEN_SESSION_LOCK_REASON);
        return access;
    }

    @Override
    @Transactional
    public UserTokenAccess refreshUserAccess(String refreshToken) {
        var access = tokenService.getAccessFromRefreshToken(refreshToken);
        var user = getUserAndValidateItsSession(access, WRONG_REFRESH_TOKEN_SESSION_LOCK_REASON);
        return grantAccess(user);
    }

    private User getUserAndValidateItsSession(InternalAccess access, String errorReason) {
        var user = userService.getUserById(access.getUserId());

        if (user.isLocked()) throw new AuthenticationException();

        if (!user.getSession().getSessionId().equals(access.getSessionId())) {
            user.lock(errorReason);
            userService.updateUser(user);
            throw new AuthenticationException();
        }

        return user;
    }

    private UserTokenAccess grantAccess(
            User user
    ) {
        var tokens = jwtFactory.generateTokens(
                new InternalAccess(
                        user.getId(),
                        user.getRole(),
                        user.getSession().getSessionId()
                )
        );

        return new UserTokenAccess(
                tokens.first(),
                authProperties.accessTokenExpirationDuration.toMillis(),
                tokens.second()
        );
    }

    @Override
    public String getPublicJwksAsJson() {
        return "{\"keys\":[" + jwtFactory.getPublicJWKAsJson() + "]}";
    }
}
