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
    private final AdminKeyStorage adminKeyStorage;


    public ClientServiceImpl(ClientApi clientApi, ClientFactory clientFactory, AdminKeyStorage adminKeyStorage) {
        this.clientApi = clientApi;
        this.clientFactory = clientFactory;
        this.adminKeyStorage = adminKeyStorage;
    }


    @Override
    public Client getClient(long id) {
        return getClientImpl(id, getToken());
    }

    @Override
    public List<Client> getAllClients() {
        return clientApi.findAll(getToken())
                .stream()
                .sorted((a, b) -> (int) (a.getId() - b.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Token createClient(String name, Set<String> clientPermissions) {
        var client = clientFactory.of(name, clientPermissions);
        return clientApi.create(client, getToken());
    }

    @Override
    public Token generateNewToken(long id) {
        return clientApi.generateNewToken(id, getToken());
    }

    @Override
    public void activateClient(long id) {
        clientApi.setActivateTo(id, true, getToken());
    }

    @Override
    public void deactivateClient(long id) {
        clientApi.setActivateTo(id, false, getToken());
    }

    @Override
    public void deleteClient(long id) {
        clientApi.deleteBy(id, getToken());
    }

    @Override
    public Token login(String password) {
        return clientApi.login(password);
    }

    @Override
    public void logout() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            adminKeyStorage.remove((String) authentication.getCredentials());
        }
    }

    @Override
    public void iAmTeapot(char[] token) {
        clientApi.iAmTeapot(token);
    }

    private char[] getToken() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            return new char[0];
        } else {
            return adminKeyStorage.get((String) authentication.getCredentials());
        }
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
