package ru.yofik.athena.admin.context.client.api.response;

import lombok.AllArgsConstructor;
import ru.yofik.athena.admin.context.client.model.ClientPermission;

import java.util.List;

@AllArgsConstructor
public class GetAllPermissionsResponse {
    public List<ClientPermission> clientPermissions;
}
