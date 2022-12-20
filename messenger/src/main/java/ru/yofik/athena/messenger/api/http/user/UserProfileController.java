package ru.yofik.athena.messenger.api.http.user;

import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.messenger.api.http.AbstractPaginationResource;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.api.http.user.view.UserView;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/userProfiles")
public class UserProfileController extends AbstractPaginationResource {
    private final UserService userService;

    public UserProfileController(ConversionService conversionService, UserService userService) {
        super(conversionService);
        this.userService = userService;
    }


    @GetMapping
    public MessengerV1Response getPageOfUsers(
            int sequentialNumber,
            int size
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                mapPage(
                        userService.getPage(new NewPage.Meta(sequentialNumber, size)),
                        UserView.class
                )
        );
    }

    @GetMapping("/{id}")
    public MessengerV1Response getUser(@PathVariable("id") long id) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        userService.getById(id),
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
