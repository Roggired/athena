package ru.yofik.athena.auth.api;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.yofik.athena.common.api.AuthV1Response;
import ru.yofik.athena.common.api.AuthV1ResponseStatus;
import ru.yofik.athena.common.api.exceptions.InvalidDataException;
import ru.yofik.athena.common.api.exceptions.NotFoundException;
import ru.yofik.athena.common.api.exceptions.UniquenessViolationException;

@ControllerAdvice
public class GlobalExceptionMapper {
    @ExceptionHandler
    public ResponseEntity<AuthV1Response> handleUniquenessViolation(
            UniquenessViolationException e
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        AuthV1Response.of(
                                AuthV1ResponseStatus.RESOURCE_ALREADY_EXISTS,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleNotFound(
            NotFoundException e
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(
                        AuthV1Response.of(
                                AuthV1ResponseStatus.RESOURCE_NOT_FOUND,
                                "Requested element not found"
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleInvalidData(
            InvalidDataException e
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        AuthV1Response.of(
                                AuthV1ResponseStatus.INVALID_DATA,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleMismatchedInput(
            MismatchedInputException e
    ) {
        return ResponseEntity
                .badRequest()
                .body(
                        AuthV1Response.of(
                                AuthV1ResponseStatus.INVALID_DATA,
                                e.getMessage()
                        )
                );
    }

    @ExceptionHandler
    public ResponseEntity<?> handleOthers(
            RuntimeException e
    ) {
        return ResponseEntity
                .internalServerError()
                .body(
                        AuthV1Response.of(
                                AuthV1ResponseStatus.UNEXPECTED_ERROR,
                                "Sorry"
                        )
                );
    }
}
