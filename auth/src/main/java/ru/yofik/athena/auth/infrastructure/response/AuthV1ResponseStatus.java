package ru.yofik.athena.auth.infrastructure.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public enum AuthV1ResponseStatus {
    DEV_ONLY(100, "DEV_ONLY"),
    RESOURCE_RETURNED(200, "RESOURCE_RETURNED"),
    RESOURCE_UPDATED(200, "RESOURCE_UPDATED"),
    RESOURCE_DELETED(200, "RESOURCE_DELETED"),
    RESOURCE_CREATED(201, "RESOURCE_CREATED"),
    MISMATCHED_REQUEST(400, "MISMATCHED_REQUEST"),
    RESOURCE_NOT_FOUND(400, "RESOURCE_NOT_FOUND"),
    RESOURCE_ALREADY_EXISTS(400, "RESOURCE_ALREADY_EXISTS"),
    INVALID_DATA(400, "INVALID_DATA"),
    UNAUTHENTICATED(401, "UNAUTHENTICATED"),
    NOT_HAVE_PERMISSION(403, "NOT_HAVE_PERMISSION"),
    DEACTIVATED(403, "DEACTIVATED"),
    UNEXPECTED_ERROR(500, "UNEXPECTED_ERROR");

    private final int httpStatusCode;
    private final String status;
}
