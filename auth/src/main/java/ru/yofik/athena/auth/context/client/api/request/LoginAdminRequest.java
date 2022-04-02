package ru.yofik.athena.auth.context.client.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class LoginAdminRequest {
    public String password;
}
