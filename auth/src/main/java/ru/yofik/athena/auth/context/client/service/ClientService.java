package ru.yofik.athena.auth.context.client.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.context.client.api.request.CreateClientRequest;
import ru.yofik.athena.auth.context.client.api.response.NewTokenResponse;
import ru.yofik.athena.auth.context.client.dto.TokenDto;
import ru.yofik.athena.auth.context.client.model.ClientPermission;
import ru.yofik.athena.auth.context.client.view.ClientView;

import java.util.List;
import java.util.Set;

@Service
public interface ClientService {
    NewTokenResponse createClient(CreateClientRequest request);
    void deactivateClient(long id);
    void activateClient(long id);
    void deleteClient(long id);
    NewTokenResponse generateNewTokenForClient(long id);
    Set<ClientPermission> authorizeClient(TokenDto tokenDto);
    ClientView getClient(long id);
    List<ClientView> getAllClients();
}
