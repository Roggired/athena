package ru.yofik.athena.admin.context.user.integration.auth.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateUserAuthRequest {
    public final String name;
    public final String login;
}
