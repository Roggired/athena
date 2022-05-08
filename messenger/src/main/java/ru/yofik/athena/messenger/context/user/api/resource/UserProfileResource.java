package ru.yofik.athena.messenger.context.user.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.context.user.api.request.UpdateUserRequest;
import ru.yofik.athena.messenger.context.user.service.UserService;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1Response;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1ResponseStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/userProfiles")
public class UserProfileResource {
    @Autowired
    private UserService userService;


    @GetMapping
    public MessengerV1Response getAllUsers() {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                userService.getAllUsers()
        );
    }

    @GetMapping("/{id}")
    public MessengerV1Response getUser(@PathVariable("id") long id) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                userService.getUser(id)
        );
    }

    @PutMapping
    public MessengerV1Response updateMyProfile(
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                userService.updateUser(request)
        );
    }
}
