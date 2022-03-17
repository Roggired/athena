package ru.yofik.messenger.controller.v1;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yofik.messenger.controller.v1.helper.ErrorMessageHelper;
import ru.yofik.messenger.model.response.v1.MessengerErrorV1Response;
import ru.yofik.messenger.model.response.v1.MessengerV1Response;
import ru.yofik.messenger.service.exception.ElementAlreadyExistsException;

@ControllerAdvice
@Slf4j
public class V1ControllerAdvice {
    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> methodArgumentNotValidExceptionHandler(
            MethodArgumentNotValidException exception
    ) {
        final var errorMessage = exception.getAllErrors().stream()
                .findFirst()
                .map(it -> {
                    if (it instanceof FieldError) {
                        final var fieldError = (FieldError) it;
                        return fieldError.getField() + " " + fieldError.getDefaultMessage();
                    } else {
                        return it.getDefaultMessage();
                    }
                })
                .orElse("");

        return ResponseEntity.badRequest().body(new MessengerErrorV1Response(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> httpMessageNotReadableExceptionHandler(
            HttpMessageNotReadableException exception
    ) {
        final String errorMessage;
        if (exception.getCause() != null && exception.getCause() instanceof MismatchedInputException) {
            errorMessage = ErrorMessageHelper.buildErrorMessageForMismatchedInputException(
                    (MismatchedInputException) exception.getCause()
            );
        } else {
            errorMessage = "";
        }

        return ResponseEntity.badRequest().body(new MessengerErrorV1Response(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> missingServletRequestParameterExceptionHandler(
            MissingServletRequestParameterException exception
    ) {
        final String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(new MessengerErrorV1Response(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> elementAlreadyExistsExceptionHandler(
            ElementAlreadyExistsException exception
    ) {
        final String errorMessage = exception.getMessage();
        return ResponseEntity.badRequest().body(new MessengerErrorV1Response(HttpStatus.BAD_REQUEST.value(), errorMessage));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> exceptionHandler(Exception exception) {
        log.error("Internal server error", exception);
        return ResponseEntity.internalServerError().body(
                new MessengerErrorV1Response(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal server error")
        );
    }
}
