package ru.yofik.athena.admin.context.client.integration.auth.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import ru.yofik.athena.admin.context.client.model.Client;

import java.util.List;

@Getter
@AllArgsConstructor
public class AllClientsAuthResponse {
    public final List<Client> clients;
}
