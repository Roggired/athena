package ru.yofik.athena.auth.api.thymeleaf;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.yofik.athena.auth.domain.auth.model.InternalAccess;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexMvcController {
    @GetMapping("/index")
    public String index(HttpServletRequest servletRequest) {
        var httpSession = servletRequest.getSession(false);
        if (httpSession == null) return "login";

        var maybeInternalAccess = httpSession.getAttribute(InternalAccess.ACCESS_SERVLET_SESSION_KEY);
        if (maybeInternalAccess != null && maybeInternalAccess.getClass() == InternalAccess.class) {
            return "redirect:/admin-panel/users-table";
        }

        return "login";
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }
}
