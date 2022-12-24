package ru.yofik.athena.auth.api.rest.admin.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@NoArgsConstructor
public class ApproveUserRegistrationRequest {
    @Positive
    public long requestId;
    @NotBlank
    public String login;
    public Boolean withNotification;
}
