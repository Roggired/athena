package ru.yofik.athena.auth.context.client.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.context.client.api.response.NewTokenResponse;

@Service
public interface AdminService {
    NewTokenResponse login(char[] password);
}
