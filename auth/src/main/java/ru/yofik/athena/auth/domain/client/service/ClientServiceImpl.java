package ru.yofik.athena.auth.domain.client.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.api.client.requests.CreateClientRequest;
import ru.yofik.athena.auth.api.client.requests.UpdateClientRequest;
import ru.yofik.athena.auth.domain.client.model.Client;
import ru.yofik.athena.auth.domain.client.repository.ClientRepository;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.api.exceptions.UniquenessViolationException;

import java.util.List;

@Service
@AllArgsConstructor
public class ClientServiceImpl implements ClientService {
    private final ClientRepository clientRepository;


    @Override
    public Client createClient(CreateClientRequest request) {
        clientRepository.findByClientId(request.clientId)
                .ifPresent(client -> {throw new UniquenessViolationException();});

        return clientRepository.save(
                Client.newClient(
                        request.clientId,
                        request.clientSecret,
                        request.redirectUrl
                )
        );
    }

    @Override
    public Client updateClient(long id, UpdateClientRequest request) {
        var client = clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);

        var updatedClient = Client.newClient(
                request.clientId,
                request.clientSecret,
                request.redirectUrl
        );
        updatedClient.setId(client.getId());

        return clientRepository.save(updatedClient);
    }

    @Override
    public void deleteClient(long id) {
        clientRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        clientRepository.deleteById(id);
    }

    @Override
    public Client getClientById(long id) {
        return clientRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }
}
