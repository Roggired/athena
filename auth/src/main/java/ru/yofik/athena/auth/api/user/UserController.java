package ru.yofik.athena.auth.api.user;

import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.api.user.views.UserShortView;
import ru.yofik.athena.auth.api.user.views.UserView;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.common.domain.NewPage;

import javax.print.attribute.standard.Media;
import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/users")
public class UserController {
    private final UserService userService;


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response createUser(
            @RequestBody @Valid CreateUserRequest request
    ) {
        request.login = request.login.trim();
        request.allowedDeviceId = request.allowedDeviceId.trim();
        if (request.password != null) request.password = request.password.trim();

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
        request.login = request.login.trim();

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
            NewPage.Meta pageMeta,
            @RequestBody @Valid FilteredUsersRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.getUsersPageable(pageMeta, request).map(UserShortView::from)
        );
    }

    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public AuthV1Response getUser(
            @PathVariable("id") long id
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                UserView.from(userService.getUser(id))
        );
    }
}
