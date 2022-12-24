package ru.yofik.athena.auth.infrastructure.config;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import ru.yofik.athena.auth.api.rest.auth.requests.AdminSignInRequest;
import ru.yofik.athena.auth.api.rest.auth.requests.ChangeAdminTemporaryPasswordRequest;
import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.domain.auth.service.AuthService;
import ru.yofik.athena.auth.domain.auth.service.PasswordNeedToBeChangedException;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.service.UserService;
import ru.yofik.athena.common.api.exceptions.NotFoundException;

import javax.validation.constraints.NotBlank;

@Configuration
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "athena.auth.preconfigured-admin")
public class PreconfiguredAdminConfig implements CommandLineRunner {
    private final UserService userService;
    private final AuthService authService;

    @Setter
    @NotBlank
    @Length(min = 5)
    private String login;
    @Setter
    @NotBlank
    @Length(min = 8)
    private String password;


    @Override
    public void run(String... args) {
        try {
            userService.getUserByLogin(login);
        } catch (NotFoundException e) {
            var request = new CreateUserRequest();
            request.login = login;
            request.email = "root";
            request.password = password;
            request.role = Role.ADMIN;
            userService.createUser(request);

            var loginRequest = new AdminSignInRequest();
            loginRequest.login = request.login;
            loginRequest.password = request.password;

            try {
                authService.loginAdmin(loginRequest);
            } catch (PasswordNeedToBeChangedException ex) {
                var changePasswordRequest = new ChangeAdminTemporaryPasswordRequest();
                changePasswordRequest.code = ex.getAdminChangePasswordResponse().changePasswordCode;
                changePasswordRequest.newPassword = request.password;
                authService.changeAdminTemporaryPassword(changePasswordRequest);
            }
        }
    }
}
