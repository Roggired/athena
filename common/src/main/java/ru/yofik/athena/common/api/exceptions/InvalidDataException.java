package ru.yofik.athena.common.api.exceptions;

public class InvalidDataException extends RuntimeException {
    public InvalidDataException() {
    }

    public InvalidDataException(String message) {
        super(message);
    }
}
