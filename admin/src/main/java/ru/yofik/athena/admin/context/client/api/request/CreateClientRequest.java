package ru.yofik.athena.admin.context.client.api.request;

import lombok.NoArgsConstructor;
import ru.yofik.athena.admin.context.client.model.ClientPermission;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.Set;

@NoArgsConstructor
public class CreateClientRequest {
    @NotBlank
    public String name;
    @NotEmpty
    public Set<ClientPermission> clientPermissions;
}
