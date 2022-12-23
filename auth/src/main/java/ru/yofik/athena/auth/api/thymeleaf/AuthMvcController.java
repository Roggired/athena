package ru.yofik.athena.auth.api.thymeleaf;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.yofik.athena.auth.api.rest.auth.requests.AdminSignInRequest;
import ru.yofik.athena.auth.api.rest.auth.requests.ChangeAdminTemporaryPasswordRequest;
import ru.yofik.athena.auth.domain.auth.model.AdminChangePasswordResponse;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;
import ru.yofik.athena.auth.domain.auth.service.AuthService;
import ru.yofik.athena.auth.domain.auth.service.PasswordNeedToBeChangedException;
import ru.yofik.athena.common.api.exceptions.AuthenticationException;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthMvcController {
    private final AuthService authService;


    @PostMapping("/login")
    public String login(
            @RequestParam("login") String login,
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest servletRequest
    ) {
        var request = new AdminSignInRequest();
        request.login = login.trim();
        request.password = password.trim();

        try {
            var internalAccess = authService.loginAdmin(request);
            servletRequest.getSession().setAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY, internalAccess);
            return "redirect:/admin-panel";
        } catch (PasswordNeedToBeChangedException e) {
            var changePassword = e.getAdminChangePasswordResponse();
            servletRequest.getSession().setAttribute("change-password-code", changePassword);
            return "reset-password";
        } catch (AuthenticationException e) {
            model.addAttribute("isAuthenticationError", true);
            model.addAttribute("login", request.login);
            return "login";
        }
    }

    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam("password") String password,
            Model model,
            HttpServletRequest servletRequest
    ) {
        var httpSession = servletRequest.getSession(false);
        if (httpSession == null) {
            return "redirect:/index";
        }

        var maybeChangePassword = httpSession.getAttribute("change-password-code");
        if (maybeChangePassword == null || maybeChangePassword.getClass() != AdminChangePasswordResponse.class) return "login";
        var changePassword = (AdminChangePasswordResponse) maybeChangePassword;

        var request = new ChangeAdminTemporaryPasswordRequest();
        request.code = changePassword.changePasswordCode;
        request.newPassword = password.trim();

        authService.changeAdminTemporaryPassword(request);
        model.addAttribute("isAfterReset", true);
        return "login";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest servletRequest, Model model) {
        authService.logout();
        var httpSession = servletRequest.getSession(false);
        if (httpSession != null) {
            servletRequest.getSession().removeAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY);
        }
        return "redirect:/index";
    }
}
