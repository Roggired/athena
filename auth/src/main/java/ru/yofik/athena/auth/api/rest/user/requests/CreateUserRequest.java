package ru.yofik.athena.auth.api.rest.user.requests;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yofik.athena.auth.domain.user.model.Role;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Length(min = 3, max = 63)
    public String login;
    @NotBlank
    @Length(min = 3, max = 255)
    public String email;
    public Role role;
    public String password;
}
