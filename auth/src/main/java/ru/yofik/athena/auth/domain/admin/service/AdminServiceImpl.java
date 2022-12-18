package ru.yofik.athena.auth.domain.admin.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.auth.api.rest.admin.requests.*;
import ru.yofik.athena.auth.domain.admin.model.NewInvitationResponse;
import ru.yofik.athena.auth.domain.auth.service.code.CodeGenerator;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.utils.SecurityUtils;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final UserService userService;
    private final CodeGenerator codeGenerator;


    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void lockUser(LockUserRequest request) {
        var user = userService.getUser(request.userId);
        if (!user.isLocked()) {
            user.lock(request.reason);
            userService.updateUser(user);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void unlockUser(UnlockUserRequest request) {
        var user = userService.getUser(request.userId);
        if (user.isLocked()) {
            user.unlock();
            userService.updateUser(user);
        }
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changePasswordForOtherAdmin(ChangePasswordForOtherAdminRequest request) {
        var otherAdmin = userService.getUser(request.userId);
        otherAdmin.getCredentials().changeAdminCredentials(
                request.newPassword,
                true
        );
        userService.updateUser(otherAdmin);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeAdminItselfPassword(ChangeAdminItselfPasswordRequest request) {
        var internalAccess = SecurityUtils.getCurrentInternalAccess();
        if (internalAccess.getRole() != Role.ADMIN) {
            throw new IllegalStateException("AccessService#changeAdminItselfPassword method can be used only by authenticated admins");
        }

        var admin = userService.getUser(internalAccess.getUserId());
        if (!admin.challengeCredentials(request.oldPassword)) {
            throw new AuthenticationException();
        }

        admin.getCredentials().changeAdminCredentials(
                request.newPassword,
                false
        );

        userService.updateUser(admin);
    }

    @Override
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public NewInvitationResponse generateUserInvitation(GenerateUserInvitationRequest request) {
        var user = userService.getUser(request.userId);
        if (user.getRole() != Role.USER) {
            throw new InvalidDataException();
        }

        var newInvitation = codeGenerator.generateLong();
        user.getCredentials().changeUserCredentials(newInvitation);
        userService.updateUser(user);

        return new NewInvitationResponse(newInvitation);
    }
}
