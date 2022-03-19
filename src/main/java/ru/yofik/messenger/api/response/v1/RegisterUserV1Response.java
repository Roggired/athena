package ru.yofik.messenger.api.response.v1;

import ru.yofik.messenger.api.view.AccessView;

public class RegisterUserV1Response extends MessengerV1Response {
    public RegisterUserV1Response(AccessView accessView) {
        super(MessengerResponseStatus.CREATED.getCode(), accessView);
    }
}
