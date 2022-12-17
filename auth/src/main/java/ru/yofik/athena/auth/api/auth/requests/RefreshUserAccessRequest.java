package ru.yofik.athena.auth.api.auth.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class RefreshUserAccessRequest {
    @NotBlank
    public String refreshToken;
}
