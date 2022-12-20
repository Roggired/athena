package ru.yofik.athena.messenger.infrastructure.integration.auth;

public class AuthServiceIntegrationException extends RuntimeException {
    public AuthServiceIntegrationException() {
    }

    public AuthServiceIntegrationException(String message) {
        super(message);
    }

    public AuthServiceIntegrationException(String message, Throwable cause) {
        super(message, cause);
    }

    public AuthServiceIntegrationException(Throwable cause) {
        super(cause);
    }

    public AuthServiceIntegrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
