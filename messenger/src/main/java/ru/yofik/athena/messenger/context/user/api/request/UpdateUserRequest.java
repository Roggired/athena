package ru.yofik.athena.messenger.context.user.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    public String login;
    @NotBlank
    public String name;
}
