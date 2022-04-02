package ru.yofik.athena.auth.context.client.api.resource;

import org.jose4j.lang.ByteUtil;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.context.client.api.request.CreateClientRequest;
import ru.yofik.athena.auth.context.client.api.request.LoginAdminRequest;
import ru.yofik.athena.auth.context.client.service.AdminService;
import ru.yofik.athena.auth.context.client.service.ClientService;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

import javax.validation.Valid;
import java.util.Base64;


@RestController
@RequestMapping("/api/v1/admin")
public class AdminClientResource {
    private final ClientService clientService;
    private final AdminService adminService;


    public AdminClientResource(ClientService clientService, AdminService adminService) {
        this.clientService = clientService;
        this.adminService = adminService;
    }


    @PostMapping("/login")
    public AuthV1Response login(@RequestBody LoginAdminRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                adminService.login(request.password.toCharArray())
        );
    }

    @GetMapping("/clients/{id}")
    public AuthV1Response getClient(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientService.getClient(id)
        );
    }

    @GetMapping("/clients")
    public AuthV1Response getAllClients() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientService.getAllClients()
        );
    }

    @PostMapping("/clients")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response createClient(@RequestBody @Valid CreateClientRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientService.createClient(request)
        );
    }

    @PostMapping("/clients/{id}/tokens")
    public AuthV1Response generateNewTokenForClient(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientService.generateNewTokenForClient(id)
        );
    }

    @PutMapping("/clients/{id}/activation")
    public AuthV1Response activateClient(@PathVariable("id") long id) {
        clientService.activateClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                "Activated"
        );
    }

    @PutMapping("/clients/{id}/deactivation")
    public AuthV1Response deactivateClient(@PathVariable("id") long id) {
        clientService.deactivateClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                "Deactivated"
        );
    }

    @DeleteMapping("/clients/{id}")
    public AuthV1Response deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                "Deleted"
        );
    }

    @PostMapping("/keys")
    @ResponseStatus(HttpStatus.CREATED)
    public AuthV1Response generateAesKey() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                Base64.getEncoder().encodeToString(ByteUtil.randomBytes(256))
        );
    }
}
