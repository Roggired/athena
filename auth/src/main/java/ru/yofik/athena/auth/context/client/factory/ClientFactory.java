package ru.yofik.athena.auth.context.client.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.context.client.dto.ClientJpaDto;
import ru.yofik.athena.auth.context.client.model.Client;
import ru.yofik.athena.auth.context.client.view.ClientView;
import ru.yofik.athena.auth.infrastructure.security.TokenGenerator;

@Component
public class ClientFactory {
    private final TokenGenerator<ClientJpaDto> tokenGenerator;


    public ClientFactory(TokenGenerator<ClientJpaDto> tokenGenerator) {
        this.tokenGenerator = tokenGenerator;
    }


    public Client from(ClientJpaDto clientJpaDto) {
        return new Client(
                clientJpaDto.getId(),
                clientJpaDto.getName(),
                clientJpaDto.isActive(),
                clientJpaDto.getClientPermissions(),
                new char[0]
        );
    }

    public Client createWithNewToken(ClientJpaDto clientJpaDto) {
        return new Client(
                clientJpaDto.getId(),
                clientJpaDto.getName(),
                true,
                clientJpaDto.getClientPermissions(),
                tokenGenerator.generateToken(clientJpaDto).getData()
        );
    }

    public ClientJpaDto toDto(Client client) {
        return new ClientJpaDto(
                client.getId(),
                client.getName(),
                client.isActive(),
                client.getClientPermissions()
        );
    }

    public ClientView toView(Client client) {
        return new ClientView(
                client.getId(),
                client.getName(),
                client.isActive(),
                client.getClientPermissions()
        );
    }
}
