package ru.yofik.athena.auth.context.client.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.yofik.athena.auth.context.client.model.ClientPermission;

import java.util.Set;

@Getter
@RequiredArgsConstructor
public class ClientView {
    public final long id;
    public final String name;
    public final boolean active;
    public final Set<ClientPermission> clientPermissions;
}
