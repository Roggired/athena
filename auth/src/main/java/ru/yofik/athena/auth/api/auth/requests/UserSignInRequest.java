package ru.yofik.athena.auth.api.auth.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor
public class UserSignInRequest {
    @Positive
    public long userId;
    @NotBlank
    public String invitation;
}
