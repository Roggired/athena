package ru.yofik.athena.admin.api.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler
    public String handleAuthentication(AuthenticationException exception) {
        return "redirect:/error";
    }

    @ExceptionHandler
    public String handleForbidden(ForbiddenException exception) {
        return "redirect:/error";
    }

    @ExceptionHandler
    public String handleHttpClientErrorUnauthorized(HttpClientErrorException.Unauthorized exception) {
        return "redirect:/error";
    }

    @ExceptionHandler
    public String handleHttpClientErrorForbidden(HttpClientErrorException.Forbidden exception) {
        return "redirect:/error";
    }

    @ExceptionHandler
    public String handleBadData(BadDataException exception) {
        return "redirect:/error";
    }
}
