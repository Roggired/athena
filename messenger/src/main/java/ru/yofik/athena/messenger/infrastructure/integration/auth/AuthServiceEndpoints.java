package ru.yofik.athena.messenger.infrastructure.integration.auth;

public enum AuthServiceEndpoints {
    USERS_GET_BY_ID("GET", "/api/v2/users/{id}"),
    USERS_UPDATE("PUT", "/api/v2/users/{id}"),
    USERS_GET_MY("GET", "/api/v2/users/my"),
    USERS_GET_FILTERED("POST", "/api/v2/users/filtered");

    private final String method;
    private final String url;

    AuthServiceEndpoints(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public String method() {
        return method;
    }

    public String url() {
        return url;
    }

    public String fullUrl() {
        return method + " " + url;
    }
}
