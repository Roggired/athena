package ru.yofik.athena.messenger.api.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;

import java.io.IOException;
import java.net.SocketException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleMismatchInput(MismatchedInputException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.MISMATCHED_REQUEST.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.MISMATCHED_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleInvalidData(InvalidDataException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.INVALID_DATA.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.INVALID_DATA, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.INVALID_DATA.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.INVALID_DATA, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleResourceNotFound(ResourceNotFoundException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.RESOURCE_NOT_FOUND.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.RESOURCE_NOT_FOUND, "See docs"));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.RESOURCE_ALREADY_EXISTS.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.RESOURCE_ALREADY_EXISTS, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleUnexpectedException(UnexpectedException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.UNEXPECTED_ERROR.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleAuthentication(AuthenticationException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.UNAUTHENTICATED.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.UNAUTHENTICATED, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleForbidden(ForbiddenException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.NOT_HAVE_PERMISSION.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.NOT_HAVE_PERMISSION, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleHttpClientErrorUnauthorized(HttpClientErrorException.Unauthorized exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.UNAUTHENTICATED.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.UNAUTHENTICATED, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleHttpClientErrorForbidden(HttpClientErrorException.Forbidden exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.NOT_HAVE_PERMISSION.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.NOT_HAVE_PERMISSION, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends MessengerV1Response> handleHttpClientErrorBadRequest(HttpClientErrorException.BadRequest exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.INVALID_DATA.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.INVALID_DATA, exception.getMessage()));
    }

    @ExceptionHandler(value = {RuntimeException.class, NullPointerException.class, SocketException.class, IOException.class})
    public ResponseEntity<? extends MessengerV1Response> handleRuntimeException(Throwable exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.UNEXPECTED_ERROR.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = {Throwable.class})
    public ResponseEntity<? extends MessengerV1Response> handleThrowable(Throwable exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(MessengerV1ResponseStatus.UNEXPECTED_ERROR.getHttpStatusCode())
                .body(MessengerV1Response.of(MessengerV1ResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }
}