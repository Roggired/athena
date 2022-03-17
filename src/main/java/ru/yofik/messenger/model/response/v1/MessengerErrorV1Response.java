package ru.yofik.messenger.model.response.v1;

public class MessengerErrorV1Response extends MessengerV1Response {
    public MessengerErrorV1Response(int status, Object payload) {
        super(status, payload);
    }
}
