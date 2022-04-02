package ru.yofik.athena.auth.context.client.api.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

@RestController
@RequestMapping("/api/v1/teapot")
public class TeapotResource {
    @GetMapping
    public AuthV1Response iAmTeapot() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                "I am teapot!"
        );
    }
}
