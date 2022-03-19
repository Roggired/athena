package ru.yofik.messenger.api.controller.v1;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yofik.messenger.api.request.v1.RegisterUserV1Request;
import ru.yofik.messenger.api.response.v1.RegisterUserV1Response;
import ru.yofik.messenger.service.auth.AuthService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/public/v1/auth")
public class AuthV1Controller {
    private final AuthService authService;

    public AuthV1Controller(AuthService authService) {
        this.authService = authService;
    }


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterUserV1Response register(@RequestBody @Valid RegisterUserV1Request request) {
        var access = authService.register(request);
        return new RegisterUserV1Response(access);
    }
}
