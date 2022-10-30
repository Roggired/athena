package ru.yofik.athena.auth.api.user.requests;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateUserRequest {
    @NotBlank
    @Length(min = 3, max = 16)
    public String login;
}
