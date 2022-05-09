package ru.yofik.athena.messenger.api.http;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class MessengerV1Response {
    public final int httpStatusCode;
    public final String status;
    public final Object payload;


    public static MessengerV1Response of(MessengerV1ResponseStatus messengerV1ResponseStatus, Object payload) {
        return new MessengerV1Response(
                messengerV1ResponseStatus.getHttpStatusCode(),
                messengerV1ResponseStatus.getStatus(),
                payload
        );
    }
}
