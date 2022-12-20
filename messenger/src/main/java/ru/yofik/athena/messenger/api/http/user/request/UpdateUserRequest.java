package ru.yofik.athena.messenger.api.http.user.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    public String login;
    @NotBlank
    public String email;
}
