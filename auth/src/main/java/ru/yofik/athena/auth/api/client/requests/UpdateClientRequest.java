package ru.yofik.athena.auth.api.client.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateClientRequest {
    @NotBlank
    public String clientId;
    @NotBlank
    public String clientSecret;
    @NotBlank
    public String redirectUrl;
}
