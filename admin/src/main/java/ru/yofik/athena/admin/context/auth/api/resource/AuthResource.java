package ru.yofik.athena.admin.context.auth.api.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.athena.admin.context.auth.service.AuthService;
import ru.yofik.athena.admin.context.auth.api.request.LoginRequest;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthResource {
    @Autowired
    private AuthService authService;


    @PostMapping("/login")
    public void login(
            @RequestBody @Valid LoginRequest request,
            HttpServletResponse servletResponse
    ) {
        authService.login(request.getPassword(), servletResponse);
    }

    @PostMapping("/logout")
    public void logout() {
        authService.logout();
    }
}
