package ru.yofik.athena.admin.context.client.integration.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Getter
public class LoginRequest {
    public final String password;
}
