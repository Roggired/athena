package ru.yofik.messenger.auth.context.client.api.resource;

import org.jose4j.lang.ByteUtil;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yofik.messenger.auth.context.client.api.request.CreateClientRequest;
import ru.yofik.messenger.auth.context.client.service.ClientService;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1Response;
import ru.yofik.messenger.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.validation.Valid;
import java.util.Base64;

@Profile("dev")
@RestController
@RequestMapping("/api/v1/admin")
public class AdminClientResource {
    private final ClientService clientService;


    public AdminClientResource(ClientService clientService) {
        this.clientService = clientService;
    }


    @GetMapping("/clients/{id}")
    public AuthV1Response getClient(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                clientService.getClient(id)
        );
    }

    @GetMapping("/clients")
    public AuthV1Response getAllClients() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                clientService.getAllClients()
        );
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response createClient(@RequestBody @Valid CreateClientRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                clientService.createClient(request)
        );
    }

    @PostMapping("/clients/{id}/tokens")
    public AuthV1Response generateNewTokenForClient(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                clientService.generateNewTokenForClient(id)
        );
    }

    @PutMapping("/clients/{id}/activation")
    public AuthV1Response activateClient(@PathVariable("id") long id) {
        clientService.activateClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                "Activated"
        );
    }

    @PutMapping("/clients/{id}/deactivated")
    public AuthV1Response deactivateClient(@PathVariable("id") long id) {
        clientService.deactivateClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                "Deactivated"
        );
    }

    @DeleteMapping("/clients/{id}")
    public AuthV1Response deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                "Deleted"
        );
    }

    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response generateAesKey() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.DEV_ONLY,
                Base64.getEncoder().encodeToString(ByteUtil.randomBytes(256))
        );
    }
}
