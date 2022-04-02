package ru.yofik.athena.admin.context.client.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.admin.context.client.factory.ClientFactory;
import ru.yofik.athena.admin.context.client.integration.ClientApi;
import ru.yofik.athena.admin.context.client.model.AdminKeyStorage;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.Token;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class ClientServiceImpl implements ClientService {
    private final ClientApi clientApi;
    private final ClientFactory clientFactory;
    private final char[] token;
    private final AdminKeyStorage adminKeyStorage;


    public ClientServiceImpl(ClientApi clientApi, ClientFactory clientFactory, AdminKeyStorage adminKeyStorage) {
        this.clientApi = clientApi;
        this.clientFactory = clientFactory;
        this.adminKeyStorage = adminKeyStorage;
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            token = new char[0];
        } else {
            token = adminKeyStorage.get((String) authentication.getCredentials());
        }
    }


    @Override
    public Client getClient(long id) {
        return getClientImpl(id, token);
    }

    @Override
    public List<Client> getAllClients() {
        return clientApi.findAll(token)
                .stream()
                .sorted((a, b) -> (int) (a.getId() - b.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Token createClient(String name, Set<String> clientPermissions) {
        var client = clientFactory.of(name, clientPermissions);
        return clientApi.create(client, token);
    }

    @Override
    public Token generateNewToken(long id) {
        return clientApi.generateNewToken(id, token);
    }

    @Override
    public void activateClient(long id) {
        clientApi.setActivateTo(id, true, token);
    }

    @Override
    public void deactivateClient(long id) {
        clientApi.setActivateTo(id, false, token);
    }

    @Override
    public void deleteClient(long id) {
        clientApi.deleteBy(id, token);
    }

    @Override
    public Token login(String password) {
        return clientApi.login(password);
    }

    @Override
    public void logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            adminKeyStorage.remove((String) authentication.getCredentials());
        }
    }

    @Override
    public void iAmTeapot() {
        clientApi.iAmTeapot(token);
    }

    private Client getClientImpl(long id, char[] token) {
        var client = clientApi.findById(id, token);
        if (client.isEmpty()) {
            log.warn(() -> "Client with id: " + id + " doesn't exist");
            throw new RuntimeException("Client with id: " + id + " doesn't exist");
        }

        return client.get();
    }
}
