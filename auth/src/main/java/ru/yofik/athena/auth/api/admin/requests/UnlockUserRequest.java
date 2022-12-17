package ru.yofik.athena.auth.api.admin.requests;

import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;

@NoArgsConstructor
public class UnlockUserRequest {
    @Positive
    public long userId;
}
