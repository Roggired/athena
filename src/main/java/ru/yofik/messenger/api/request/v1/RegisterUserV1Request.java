package ru.yofik.messenger.api.request.v1;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class RegisterUserV1Request {
    @NotBlank
    @Length(min = 4, max = 20)
    private final String login;
    @NotBlank
    @Length(min = 4, max = 20)
    private final String password;
}
