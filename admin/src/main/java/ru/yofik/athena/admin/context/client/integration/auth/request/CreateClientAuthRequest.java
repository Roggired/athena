package ru.yofik.athena.admin.context.client.integration.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class CreateClientAuthRequest {
    public final String name;
    public final Set<String> clientPermissions;
}
