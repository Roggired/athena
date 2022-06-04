package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.Message;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ChangedMessageNotification extends ChatNotification {
    private final ChangedMessagePayload changedMessagePayload;

    public ChangedMessageNotification(NotificationType type, Chat chat, Message message, long userId) {
        super(type, chat, message);
        this.changedMessagePayload = new ChangedMessagePayload(userId, message);
    }
}
