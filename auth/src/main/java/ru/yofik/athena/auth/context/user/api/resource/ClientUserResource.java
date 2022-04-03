package ru.yofik.athena.auth.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.auth.context.user.api.request.AuthorizeUserRequest;
import ru.yofik.athena.auth.context.user.service.UserService;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class ClientUserResource {
    @Autowired
    private UserService userService;


    @PostMapping("/invitations")
    public AuthV1Response activateUser(@Valid @RequestBody ActivateUserRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                userService.activate(request)
        );
    }

    @PostMapping("/authorities")
    public AuthV1Response authorizeUser(@Valid @RequestBody AuthorizeUserRequest request, HttpServletRequest servletRequest) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.authorize(request, servletRequest.getHeader("User-Agent"))
        );
    }
}
