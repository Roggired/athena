package ru.yofik.athena.auth.api.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yofik.athena.auth.api.auth.requests.AdminSignInRequest;
import ru.yofik.athena.auth.domain.auth.model.Access;
import ru.yofik.athena.auth.domain.auth.service.AccessService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v2/auth")
public class AuthController {
    private final AccessService accessService;


    @PostMapping("/admins/sign-in")
    public void adminSignIn(
            @RequestBody @Valid AdminSignInRequest request,
            HttpServletRequest servletRequest
    ) {
       request.login = request.login.trim();
       request.password = request.password.trim();

       var adminAccess = accessService.loginAdmin(request.login, request.password);
       var session = servletRequest.getSession();
       session.setAttribute(Access.ACCESS_SERVLET_SESSION_KEY, adminAccess);
    }

    @PostMapping("/admins/sign-out")
    public void adminSignOut(
            HttpServletRequest request
    ) {
        request.getSession().removeAttribute(Access.ACCESS_SERVLET_SESSION_KEY);
    }
}
