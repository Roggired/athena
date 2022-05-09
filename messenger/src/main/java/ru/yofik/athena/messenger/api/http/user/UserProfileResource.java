package ru.yofik.athena.messenger.api.http.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.api.http.user.view.UserView;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/userProfiles")
public class UserProfileResource {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;


    @GetMapping
    public MessengerV1Response getAllUsers() {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                userService.getAllUsers()
                        .stream()
                        .map(user -> conversionService.convert(user, UserView.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public MessengerV1Response getUser(@PathVariable("id") long id) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        userService.getUser(id),
                        UserView.class
                )
        );
    }

    @PutMapping
    public MessengerV1Response updateMyProfile(
            @RequestBody @Valid UpdateUserRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                conversionService.convert(
                        userService.updateUser(request),
                        UserView.class
                )
        );
    }
}
