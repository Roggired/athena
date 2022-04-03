package ru.yofik.athena.auth.context.user.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class ActivateUserRequest {
    @NotBlank
    public String code;
}
