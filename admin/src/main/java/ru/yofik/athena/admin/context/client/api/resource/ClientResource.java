package ru.yofik.athena.admin.context.client.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.admin.context.client.api.request.CreateClientRequest;
import ru.yofik.athena.admin.context.client.api.response.GetAllClientsResponse;
import ru.yofik.athena.admin.context.client.api.response.GetAllPermissionsResponse;
import ru.yofik.athena.admin.context.client.api.response.NewTokenResponse;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.ClientPermission;
import ru.yofik.athena.admin.context.client.service.ClientService;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/clients")
public class ClientResource {
    @Autowired
    private ClientService clientService;


    @GetMapping
    public GetAllClientsResponse getAllClients() {
        return new GetAllClientsResponse(clientService.getAllClients());
    }

    @GetMapping("/{id}")
    public Client getClient(
            @PathVariable("id") long id
    ) {
        return clientService.getClient(id);
    }

    @PostMapping
    public void createClient(
            @RequestBody @Valid CreateClientRequest request
    ) {
        clientService.createClient(request.name, request.clientPermissions);
    }

    @DeleteMapping("/{id}")
    public void deleteClient(
            @PathVariable("id") long id
    ) {
        clientService.deleteClient(id);
    }

    @PutMapping("/{id}/activate")
    public void activateClient(
            @PathVariable("id") long id
    ) {
        clientService.activateClient(id);
    }

    @PutMapping("/{id}/deactivate")
    public void deactivateClient(
            @PathVariable("id") long id
    ) {
        clientService.deactivateClient(id);
    }

    @GetMapping("/permissions")
    public GetAllPermissionsResponse getAvailablePermissions() {
        return new GetAllPermissionsResponse(
                Arrays.stream(ClientPermission.values())
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}/token")
    public NewTokenResponse generateNewToken(
            @PathVariable("id") long id
    ) {
        return new NewTokenResponse(
                new String(clientService.generateNewToken(id).getToken())
        );
    }
}
