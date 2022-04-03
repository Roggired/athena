package ru.yofik.athena.auth.api.exception;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yofik.athena.auth.infrastructure.response.AuthV1Response;
import ru.yofik.athena.auth.infrastructure.response.AuthV1ResponseStatus;

import java.io.IOException;
import java.net.SocketException;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleUnauthenticated(UnauthenticatedException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.UNAUTHENTICATED.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.UNAUTHENTICATED, "Fuck off!!!"));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleNotHavePermission(NotHavePermission exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.NOT_HAVE_PERMISSION.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.NOT_HAVE_PERMISSION, "You do not have permission for this action"));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleMismatchInput(MismatchedInputException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.MISMATCHED_REQUEST.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.MISMATCHED_REQUEST, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleInvalidData(InvalidDataException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.INVALID_DATA.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.INVALID_DATA, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleResourceNotFound(ResourceNotFoundException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.RESOURCE_NOT_FOUND.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.RESOURCE_NOT_FOUND, "See docs"));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleResourceAlreadyExists(ResourceAlreadyExistsException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.RESOURCE_ALREADY_EXISTS.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.RESOURCE_ALREADY_EXISTS, exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<? extends AuthV1Response> handleUnexpectedException(UnexpectedException exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.UNEXPECTED_ERROR.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }

    @ExceptionHandler(value = {RuntimeException.class, NullPointerException.class, SocketException.class, IOException.class})
    public ResponseEntity<? extends AuthV1Response> handleRuntimeException(Throwable exception) {
        log.warn("", exception);
        return ResponseEntity
                .status(AuthV1ResponseStatus.UNEXPECTED_ERROR.getHttpStatusCode())
                .body(AuthV1Response.of(AuthV1ResponseStatus.UNEXPECTED_ERROR, exception.getMessage()));
    }
}