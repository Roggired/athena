package ru.yofik.athena.auth.domain.admin.service;

import ru.yofik.athena.auth.api.admin.requests.*;
import ru.yofik.athena.auth.domain.admin.model.NewInvitationResponse;

public interface AdminService {
    void lockUser(LockUserRequest request);
    void unlockUser(UnlockUserRequest request);

    void changePasswordForOtherAdmin(ChangePasswordForOtherAdminRequest request);
    void changeAdminItselfPassword(ChangeAdminItselfPasswordRequest request);

    NewInvitationResponse generateUserInvitation(GenerateUserInvitationRequest request);
}
