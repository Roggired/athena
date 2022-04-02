package ru.yofik.athena.auth.context.client.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.api.exception.UnauthenticatedException;
import ru.yofik.athena.auth.context.client.api.response.NewTokenResponse;

import java.util.Arrays;

@Service
public class AdminServiceImpl implements AdminService {
    @Value("${yofik.security.admin-password}")
    private char[] adminPassword;

    private final ClientService clientService;

    public AdminServiceImpl(ClientService clientService) {
        this.clientService = clientService;
    }

    @Override
    public NewTokenResponse login(char[] password) {
        if (Arrays.equals(adminPassword, password)) {
            return clientService.generateNewTokenForClient(1);
        }

        throw new UnauthenticatedException();
    }
}
