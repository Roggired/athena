package ru.yofik.messenger.model.response.v1;

import lombok.Getter;

@Getter
public enum MessengerResponseStatus {
    CREATED(201),
    OK(200);

    private final int code;

    MessengerResponseStatus(int code) {
        this.code = code;
    }
}
