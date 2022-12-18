package ru.yofik.athena.auth.api.rest.auth.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class ChangeAdminTemporaryPasswordRequest {
    @NotBlank
    public String code;
    @NotBlank
    public String newPassword;
}
