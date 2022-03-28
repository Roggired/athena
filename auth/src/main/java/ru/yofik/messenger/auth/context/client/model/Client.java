package ru.yofik.messenger.auth.context.client.model;

import lombok.Getter;
import lombok.Setter;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public final class Client {
    private final long id;
    private String name;
    private boolean active;
    private final Set<ClientPermission> clientPermissions;
    private char[] accessToken;


    public Client(long id,
                  String name,
                  boolean active,
                  Set<ClientPermission> clientPermissions,
                  char[] accessToken) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.clientPermissions = clientPermissions == null ? new HashSet<>() : clientPermissions;
        this.accessToken = accessToken;
    }


    public void addPermission(ClientPermission clientPermission) {
        clientPermissions.add(clientPermission);
    }

    public void removePermission(ClientPermission clientPermission) {
        clientPermissions.remove(clientPermission);
    }

    public boolean hasPermission(ClientPermission clientPermission) {
        return clientPermissions.contains(clientPermission);
    }
}
