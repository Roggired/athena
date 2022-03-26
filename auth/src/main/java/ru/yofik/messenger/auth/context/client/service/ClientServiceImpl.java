package ru.yofik.messenger.auth.context.client.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.messenger.auth.api.exception.ResourceAlreadyExistsException;
import ru.yofik.messenger.auth.api.exception.ResourceNotFoundException;
import ru.yofik.messenger.auth.api.exception.UnexpectedException;
import ru.yofik.messenger.auth.context.client.api.request.CreateClientRequest;
import ru.yofik.messenger.auth.context.client.api.response.NewTokenResponse;
import ru.yofik.messenger.auth.context.client.dto.ClientJpaDto;
import ru.yofik.messenger.auth.context.client.dto.TokenDto;
import ru.yofik.messenger.auth.context.client.factory.ClientFactory;
import ru.yofik.messenger.auth.context.client.model.Client;
import ru.yofik.messenger.auth.context.client.model.ClientPermission;
import ru.yofik.messenger.auth.context.client.repository.ClientRepository;
import ru.yofik.messenger.auth.context.client.view.ClientView;
import ru.yofik.messenger.auth.context.client.view.TokenView;
import ru.yofik.messenger.auth.infrastructure.security.InvalidTokenException;
import ru.yofik.messenger.auth.infrastructure.security.Token;
import ru.yofik.messenger.auth.infrastructure.security.TokenVerifier;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Log4j2
public class ClientServiceImpl implements ClientService {
    private final TokenVerifier<ClientJpaDto> tokenVerifier;
    private final ClientFactory clientFactory;
    private final ClientRepository clientRepository;


    public ClientServiceImpl(TokenVerifier<ClientJpaDto> tokenVerifier,
                             ClientFactory clientFactory,
                             ClientRepository clientRepository) {
        this.tokenVerifier = tokenVerifier;
        this.clientFactory = clientFactory;
        this.clientRepository = clientRepository;
    }


    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public NewTokenResponse createClient(CreateClientRequest request) {
        if (isClientExists(request.name)) {
            log.warn(() -> "Client with name: " + request.name + " already exists");
            throw new ResourceAlreadyExistsException("Client name must be unique");
        }

        var newClientJpaDto = new ClientJpaDto();
        newClientJpaDto.setName(request.name);
        newClientJpaDto.setActive(true);
        newClientJpaDto.setClientPermissions(request.clientPermissions);

        var clientJpaDto = clientRepository.save(newClientJpaDto);
        var client = clientFactory.createWithNewToken(clientJpaDto);

        var response = new NewTokenResponse(new String(client.getAccessToken()));
        client.setAccessToken(new char[0]);

        return response;
    }

    @Override
    public void deactivateClient(long id) {
        var client = getClientInternal(id);
        client.setActive(false);
        saveClient(client);
    }

    @Override
    public void activateClient(long id) {
        var client = getClientInternal(id);
        client.setActive(true);
        saveClient(client);
    }

    @Override
    public void deleteClient(long id) {
        var client = getClientInternal(id);
        clientRepository.deleteById(client.getId());
    }

    @Override
    public NewTokenResponse generateNewTokenForClient(long id) {
        var client = getClientInternal(id);
        client = clientFactory.createWithNewToken(clientFactory.toDto(client));
        var response = new NewTokenResponse(new String(client.getAccessToken()));
        client.setAccessToken(new char[0]);
        return response;
    }

    @Override
    public Set<ClientPermission> authorizeClient(TokenDto tokenDto) {
        ClientJpaDto clientJpaDto;
        try {
            clientJpaDto = tokenVerifier.verify(
                    new Token(tokenDto.getToken().toCharArray(), Token.Type.JWE),
                    ClientJpaDto.class
            );
        } catch (InvalidTokenException e) {
            log.warn(() -> "Invalid token");
            throw new UnexpectedException();
        }

        return clientJpaDto.getClientPermissions();
    }

    @Override
    public ClientView getClient(long id) {
        var client = getClientInternal(id);
        return clientFactory.toView(client);
    }

    @Override
    public List<ClientView> getAllClients() {
        return clientRepository.findAll()
                .stream()
                .map(dto -> clientFactory.toView(clientFactory.from(dto)))
                .collect(Collectors.toList());
    }

    private Client getClientInternal(long id) {
        Optional<ClientJpaDto> clientJpaDto = clientRepository.findById(id);
        if (clientJpaDto.isEmpty()) {
            log.warn(() -> "No client with id: " + id);
            throw new ResourceNotFoundException();
        }

        return clientFactory.from(clientJpaDto.get());
    }

    private void saveClient(Client client) {
        var clientJpaDto = clientFactory.toDto(client);
        clientRepository.save(clientJpaDto);
    }

    private boolean isClientExists(String name) {
        Optional<ClientJpaDto> clientJpaDto = clientRepository.findByName(name);
        return clientJpaDto.isPresent();
    }
}
