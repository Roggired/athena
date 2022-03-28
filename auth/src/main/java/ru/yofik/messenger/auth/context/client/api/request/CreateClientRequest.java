package ru.yofik.messenger.auth.context.client.api.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yofik.messenger.auth.context.client.model.ClientPermission;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@AllArgsConstructor
public class CreateClientRequest {
    @NotBlank
    public final String name;
    @NotNull
    public final Set<ClientPermission> clientPermissions;
}
