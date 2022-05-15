package ru.yofik.athena.messenger.api.ws.notification.view;

import lombok.AllArgsConstructor;
import ru.yofik.athena.messenger.api.http.user.view.UserView;

@AllArgsConstructor
public class ChangedUserPayloadView {
    public final UserView user;
    public final long chatId;
}
