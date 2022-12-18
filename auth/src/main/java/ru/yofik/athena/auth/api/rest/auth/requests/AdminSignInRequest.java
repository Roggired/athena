package ru.yofik.athena.auth.api.rest.auth.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class AdminSignInRequest {
    @NotBlank
    public String login;
    @NotBlank
    public String password;
}
