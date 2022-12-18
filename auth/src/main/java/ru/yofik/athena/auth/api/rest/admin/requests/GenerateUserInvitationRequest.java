package ru.yofik.athena.auth.api.rest.admin.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@NoArgsConstructor
public class GenerateUserInvitationRequest {
    @Positive
    public long userId;
}
