package ru.yofik.athena.auth.api.client;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.auth.api.client.requests.CreateClientRequest;
import ru.yofik.athena.auth.api.client.requests.UpdateClientRequest;
import ru.yofik.athena.auth.api.client.views.ClientView;
import ru.yofik.athena.auth.domain.client.service.ClientService;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;

import javax.validation.Valid;
import java.util.stream.Collectors;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v2/clients")
public class ClientController {
    private final ClientService clientService;


    @PostMapping
    public AuthV1Response createClient(@RequestBody @Valid CreateClientRequest request) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_CREATED,
                ClientView.from(clientService.createClient(request))
        );
    }

    @PutMapping("/{id}")
    public AuthV1Response updateClient(
            @PathVariable("id") long id,
            @RequestBody @Valid UpdateClientRequest request
    ) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_UPDATED,
                ClientView.from(clientService.updateClient(id, request))
        );
    }

    @DeleteMapping("/{id}")
    public AuthV1Response deleteClient(@PathVariable("id") long id) {
        clientService.deleteClient(id);
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_DELETED,
                "Deleted"
        );
    }

    @GetMapping("/{id}")
    public AuthV1Response getClient(@PathVariable("id") long id) {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                ClientView.from(clientService.getClientById(id))
        );
    }

    @GetMapping
    public AuthV1Response getAllClients() {
        return AuthV1Response.of(
                AuthV1ResponseStatus.RESOURCE_RETURNED,
                clientService.getAllClients()
                        .stream()
                        .map(ClientView::from)
                        .collect(Collectors.toList())
        );
    }
}
