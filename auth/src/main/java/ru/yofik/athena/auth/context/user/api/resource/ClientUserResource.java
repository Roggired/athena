package ru.yofik.athena.auth.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.auth.context.user.api.request.AuthorizeUserRequest;
import ru.yofik.athena.auth.context.user.service.UserService;
import ru.yofik.athena.auth.context.user.view.ClientUserView;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/users")
public class ClientUserResource {
    @Autowired
    private UserService userService;


    @GetMapping
    public AuthV1Response getAllUsers() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.getAll()
                        .stream()
                        .map(shortView -> new ClientUserView(shortView.id, shortView.name, shortView.login))
                        .collect(Collectors.toList())
        );
    }

    @PostMapping("/invitations")
    public AuthV1Response activateUser(@Valid @RequestBody ActivateUserRequest request, HttpServletRequest servletRequest) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                userService.activate(request, servletRequest.getHeader("User-Agent"))
        );
    }

    @PostMapping("/authorities")
    public AuthV1Response authorizeUser(@Valid @RequestBody AuthorizeUserRequest request, HttpServletRequest servletRequest) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                userService.authorize(request, servletRequest.getHeader("User-Agent"))
        );
    }

    @GetMapping("/{id}")
    public AuthV1Response getUser(@PathVariable("id") long id) {
        var userView = userService.get(id);
        var clientUserView = new ClientUserView(userView.id, userView.name, userView.login);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientUserView
        );
    }
}
