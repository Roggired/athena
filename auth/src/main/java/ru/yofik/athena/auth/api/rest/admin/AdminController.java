package ru.yofik.athena.auth.api.rest.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.rest.admin.requests.*;
import ru.yofik.athena.auth.domain.admin.service.AdminService;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/user-management")
public class AdminController {
    private final AdminService adminService;

    @PostMapping("/locks")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response lockUser(
            @RequestBody @Valid LockUserRequest request
    ) {
        adminService.lockUser(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                ""
        );
    }

    @DeleteMapping("/locks/{userId}")
    public AuthV1Response unlockUser(
            @PathVariable("userId") long userId
    ) {
        var request = new UnlockUserRequest();
        request.userId = userId;
        adminService.unlockUser(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_DELETED,
                ""
        );
    }

    @PostMapping("/passwords/my")
    public AuthV1Response changeItselfPassword(
            @RequestBody @Valid ChangeAdminItselfPasswordRequest request
    ) {
        adminService.changeAdminItselfPassword(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                ""
        );
    }

    @PostMapping("/password/other")
    public AuthV1Response changeOtherAdminPassword(
            @RequestBody @Valid ChangePasswordForOtherAdminRequest request
    ) {
        adminService.changePasswordForOtherAdmin(request);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                ""
        );
    }

    @PostMapping("/invitations")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response generateInvitationForUser(
            @RequestBody @Valid GenerateUserInvitationRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                adminService.generateUserInvitation(request)
        );
    }
}
