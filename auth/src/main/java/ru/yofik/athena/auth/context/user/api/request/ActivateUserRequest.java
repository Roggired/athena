package ru.yofik.athena.auth.context.user.api.request;

import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@ToString
public class ActivateUserRequest {
    @NotBlank
    public String code;
}
