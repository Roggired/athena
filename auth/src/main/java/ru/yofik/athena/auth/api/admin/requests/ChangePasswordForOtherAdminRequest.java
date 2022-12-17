package ru.yofik.athena.auth.api.admin.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor
public class ChangePasswordForOtherAdminRequest {
    @Positive
    public long userId;
    @NotBlank
    public String newPassword;
}
