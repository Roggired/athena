package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class NewUserNotification extends ChatNotification {
    private final ChangedUserPayload changedUserPayload;


    public NewUserNotification(
            Chat chat,
            User user,
            long chatId) {
        super(NotificationType.NEW_USER, chat);
        this.changedUserPayload = new ChangedUserPayload(user, chatId);
    }
}
