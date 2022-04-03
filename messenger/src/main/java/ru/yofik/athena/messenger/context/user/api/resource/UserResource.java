package ru.yofik.athena.messenger.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.athena.messenger.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.messenger.context.user.api.request.AuthorizeUserRequest;
import ru.yofik.athena.messenger.context.user.service.UserService;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1Response;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1ResponseStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserResource {
    @Autowired
    private UserService userService;

    @PostMapping("/authorization")
    public MessengerV1Response authorizeUser(@Valid @RequestBody AuthorizeUserRequest request) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                userService.authorizeUser(request.accessToken)
        );
    }

    @PostMapping("/activation")
    public MessengerV1Response activateUser(@Valid @RequestBody ActivateUserRequest request) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                userService.activate(request)
        );
    }
}
