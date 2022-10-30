package ru.yofik.athena.common.api.exceptions;

public class UniquenessViolationException extends RuntimeException {
    public UniquenessViolationException(String message) {
        super(message);
    }

    public UniquenessViolationException() {
    }
}
