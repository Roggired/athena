package ru.yofik.messenger.auth.context.client.api.resource;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1Response;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1ResponseStatus;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientResource {
    @GetMapping
    public AuthV1Response helloWorld() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                "Hello, World!"
        );
    }
}
