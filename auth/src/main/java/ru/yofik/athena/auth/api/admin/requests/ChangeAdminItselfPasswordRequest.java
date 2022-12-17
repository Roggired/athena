package ru.yofik.athena.auth.api.admin.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class ChangeAdminItselfPasswordRequest {
    @NotBlank
    public String oldPassword;
    @NotBlank
    public String newPassword;
}
