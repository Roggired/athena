package ru.yofik.athena.admin.context.client.integration;

import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.Token;

import java.util.List;
import java.util.Optional;

public interface ClientApi {
    Optional<Client> findById(long id, char[] token);
    List<Client> findAll(char[] token);
    Token create(Client client, char[] token);
    Token generateNewToken(long id, char[] token);
    void setActivateTo(long id, boolean newValue, char[] token);
    void deleteBy(long id, char[] token);
    Token login(String password);
    void iAmTeapot(char[] token);
}
