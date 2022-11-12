package ru.yofik.athena.auth.api.client.views;

import lombok.AllArgsConstructor;
import ru.yofik.athena.auth.domain.client.model.Client;

@AllArgsConstructor
public class ClientView {
    public long id;
    public String clientId;
    public String redirectUrl;

    public static ClientView from(Client client) {
        return new ClientView(
                client.getId(),
                client.getClientId(),
                client.getRedirectUrl()
        );
    }
}
