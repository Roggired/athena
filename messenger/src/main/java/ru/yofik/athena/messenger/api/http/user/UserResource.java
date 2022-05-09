package ru.yofik.athena.messenger.api.http.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.user.request.ActivateUserRequest;
import ru.yofik.athena.messenger.api.http.user.request.AuthorizeUserRequest;
import ru.yofik.athena.messenger.api.http.user.view.AccessTokenView;
import ru.yofik.athena.messenger.api.http.user.view.UserView;
import ru.yofik.athena.messenger.domain.user.service.UserService;
import ru.yofik.athena.messenger.infrastructure.integration.auth.AthenaAuthApi;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/users")
public class UserResource {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private UserService userService;
    @Autowired
    private AthenaAuthApi athenaAuthApi;


    @PostMapping("/authorization")
    public MessengerV1Response authorizeUser(
            @Valid @RequestBody AuthorizeUserRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        athenaAuthApi.authorize(request.accessToken),
                        UserView.class
                )
        );
    }

    @PostMapping("/activation")
    public MessengerV1Response activateUser(
            @Valid @RequestBody ActivateUserRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        athenaAuthApi.activate(request.invitation),
                        AccessTokenView.class
                )
        );
    }
}
