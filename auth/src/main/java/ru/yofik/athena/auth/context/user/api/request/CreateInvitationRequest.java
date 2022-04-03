package ru.yofik.athena.auth.context.user.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@NoArgsConstructor
public class CreateInvitationRequest {
    @Positive
    public long userId;
    @Positive
    public int count;
}
