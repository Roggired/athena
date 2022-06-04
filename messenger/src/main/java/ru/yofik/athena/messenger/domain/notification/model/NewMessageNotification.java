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
public class NewMessageNotification extends ChatNotification {
    private final Message message;

    public NewMessageNotification(Chat chat, Message message) {
        super(NotificationType.NEW_MESSAGE, chat);
        this.message = message;
    }
}
