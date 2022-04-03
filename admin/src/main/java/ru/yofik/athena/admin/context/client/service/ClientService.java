package ru.yofik.athena.admin.context.client.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.Token;

import java.util.List;
import java.util.Set;

@Service
public interface ClientService {
    Client getClient(long id);
    List<Client> getAllClients();
    Token createClient(String name, Set<String> clientPermissions);
    Token generateNewToken(long id);
    void activateClient(long id);
    void deactivateClient(long id);
    void deleteClient(long id);
    Token login(String password);
    void logout();
    void iAmTeapot(char[] token);
}
