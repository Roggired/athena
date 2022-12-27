package ru.yofik.athena.auth.api.rest.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.rest.admin.requests.*;
import ru.yofik.athena.auth.api.rest.user.views.UserView;
import ru.yofik.athena.auth.domain.admin.service.AdminService;
import ru.yofik.athena.auth.domain.auth.service.UserRegistrationRequestService;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.common.domain.NewPage;

import javax.validation.Valid;

@CrossOrigin
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/user-management")
public class AdminController {
    private final AdminService adminService;
    private final UserRegistrationRequestService userRegistrationRequestService;

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

    @PostMapping("/registration-requests/filtered")
    public AuthV1Response getUserRegistrationRequests(
            NewPage.Meta pageMeta,
            @RequestBody @Valid FilteredUserRegistrationRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userRegistrationRequestService.getPage(pageMeta, request)
        );
    }

    @PostMapping("/registration-requests/approve")
    public AuthV1Response approveUserRegistrationRequest(
            @RequestBody @Valid ApproveUserRegistrationRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.OPERATION_OK,
                UserView.from(userRegistrationRequestService.approve(request))
        );
    }
}
