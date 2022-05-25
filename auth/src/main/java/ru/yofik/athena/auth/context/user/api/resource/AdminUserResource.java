package ru.yofik.athena.auth.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.context.user.api.request.CreateInvitationRequest;
import ru.yofik.athena.auth.context.user.api.request.CreateUserRequest;
import ru.yofik.athena.auth.context.user.model.LockReason;
import ru.yofik.athena.auth.context.user.service.UserService;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/admin/users")
public class AdminUserResource {
    @Autowired
    private UserService userService;


    @PostMapping
    public AuthV1Response createUser(@Valid @RequestBody CreateUserRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                userService.create(request)
        );
    }

    @GetMapping("/{id}")
    public AuthV1Response getUser(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.get(id)
        );
    }

    @GetMapping
    public AuthV1Response getAllUsers() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.getAll()
        );
    }

    @DeleteMapping("/{id}")
    public AuthV1Response deleteUser(@PathVariable("id") long id) {
        userService.delete(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_DELETED,
                "User has been deleted"
        );
    }

    @PostMapping("/{id}/lock")
    public AuthV1Response lockUserManually(@PathVariable("id") long id) {
        userService.lock(id, LockReason.ADMIN_MANUALLY);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                "User has been locked by admin"
        );
    }

    @DeleteMapping("/{id}/lock")
    public AuthV1Response unlockUser(@PathVariable("id") long id) {
        userService.unlock(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                "User has been unlocked"
        );
    }

    @PutMapping("/{id}/invitations")
    public AuthV1Response createInvitation(@PathVariable("id") long id, @RequestBody CreateInvitationRequest request) {
        request.userId = id;
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                userService.createInvitation(request)
        );
    }
}
