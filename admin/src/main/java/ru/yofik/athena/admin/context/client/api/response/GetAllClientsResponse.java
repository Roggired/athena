package ru.yofik.athena.admin.context.client.api.response;

import lombok.AllArgsConstructor;
import ru.yofik.athena.admin.context.client.model.Client;

import java.util.List;

@AllArgsConstructor
public class GetAllClientsResponse {
    public List<Client> clients;
}
