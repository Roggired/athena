package ru.yofik.athena.admin.context.auth.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.admin.context.client.model.Token;

import javax.servlet.http.HttpServletResponse;

@Service
public interface AuthService {
    Token login(String password, HttpServletResponse servletResponse);
    void logout();
}
