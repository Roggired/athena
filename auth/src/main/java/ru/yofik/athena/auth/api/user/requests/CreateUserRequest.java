package ru.yofik.athena.auth.api.user.requests;

import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.yofik.athena.auth.domain.user.model.Role;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Length(min = 3, max = 16)
    public String login;
    @NotBlank
    public String allowedDeviceId;
    public Role role;
    public String password;
}
