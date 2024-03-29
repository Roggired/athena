package ru.yofik.athena.admin.context.client.model;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public final class Client {
    private final long id;
    @NotBlank
    private String name;
    private Boolean active;
    @NotEmpty
    private final Set<ClientPermission> clientPermissions;


    public Client(long id,
                  String name,
                  Boolean active,
                  Set<ClientPermission> clientPermissions) {
        this.id = id;
        this.name = name;
        this.active = active;
        this.clientPermissions = clientPermissions == null ? new HashSet<>() : clientPermissions;
    }
}
