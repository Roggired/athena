package ru.yofik.athena.admin.context.client.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.admin.context.client.model.Client;
import ru.yofik.athena.admin.context.client.model.ClientPermission;

import java.util.Set;

@Component
public class ClientFactory {
    public Client of(String name, Set<ClientPermission> clientPermissions) {
        return new Client(0L,
                name,
                true,
                clientPermissions
        );
    }
}
