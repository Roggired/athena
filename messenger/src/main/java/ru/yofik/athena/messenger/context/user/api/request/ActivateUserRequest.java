package ru.yofik.athena.messenger.context.user.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class ActivateUserRequest {
    @NotBlank
    public String invitation;
}
