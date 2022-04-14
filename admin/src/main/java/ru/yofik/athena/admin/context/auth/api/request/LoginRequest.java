package ru.yofik.athena.admin.context.auth.api.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;

@NoArgsConstructor
@Getter
@Setter
public class LoginRequest {
    @Valid
    public String password;
}
