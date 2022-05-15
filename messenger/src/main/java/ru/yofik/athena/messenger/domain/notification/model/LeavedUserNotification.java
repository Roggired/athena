package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class LeavedUserNotification extends ChatNotification {
    private final ChangedUserPayload changedUserPayload;


    public LeavedUserNotification(
            Chat chat,
            User user,
            long chatId
    ) {
        super(NotificationType.LEAVED_USER, chat);
        this.changedUserPayload = new ChangedUserPayload(user, chatId);
    }
}
