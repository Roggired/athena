package ru.yofik.athena.auth.domain.client.service;

import ru.yofik.athena.auth.api.client.requests.CreateClientRequest;
import ru.yofik.athena.auth.api.client.requests.UpdateClientRequest;
import ru.yofik.athena.auth.domain.client.model.Client;

import java.util.List;

public interface ClientService {
    Client createClient(CreateClientRequest request);
    Client updateClient(long id, UpdateClientRequest request);
    void deleteClient(long id);
    Client getClientById(long id);
    List<Client> getAllClients();
}
