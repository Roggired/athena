package ru.yofik.athena.auth.api.rest.user;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.rest.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.api.rest.user.views.UserShortView;
import ru.yofik.athena.auth.api.rest.user.views.UserView;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.auth.utils.SecurityUtils;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.common.domain.NewPage;

import javax.validation.Valid;

@CrossOrigin
@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    private final UserService userService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                UserView.from(userService.createUser(request))
        );
    }

    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response updateUser(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                UserView.from(userService.updateUser(id, request))
        );
    }

    @DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response deleteUser(
            @PathVariable("id") long id
    ) {
        userService.deleteUser(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_DELETED,
                "Deleted"
        );
    }

    @PostMapping(value = "/filtered", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response getUsersPageable(
            int sequentialNumber,
            int size,
            @RequestBody @Valid FilteredUsersRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.getUsersPageable(new NewPage.Meta(sequentialNumber, size), request).map(UserShortView::from)
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response getUser(
            @PathVariable("id") long id
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                UserView.from(userService.getUserById(id))
        );
    }

    @GetMapping("/my")
    public AuthV1Response getMyUser() {
        var internalAccess = SecurityUtils.getCurrentInternalAccess();
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                UserView.from(userService.getUserById(internalAccess.getUserId()))
        );
    }
}
