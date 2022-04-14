package ru.yofik.athena.admin.context.auth.integration;

import ru.yofik.athena.admin.context.client.model.Token;

public interface AuthApi {
    Token login(String password);
}
