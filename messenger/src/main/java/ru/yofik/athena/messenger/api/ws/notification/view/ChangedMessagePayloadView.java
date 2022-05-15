package ru.yofik.athena.messenger.api.ws.notification.view;

import lombok.AllArgsConstructor;
import ru.yofik.athena.messenger.api.http.chat.view.MessageView;

@AllArgsConstructor
public class ChangedMessagePayloadView {
    public final long initiatorId;
    public final MessageView message;
}
