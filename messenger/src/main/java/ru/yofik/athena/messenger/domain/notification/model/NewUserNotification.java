package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class NewUserNotification extends Notification {
    private final ChangedUserPayload changedUserPayload;


    public NewUserNotification(
            List<Long> targetUserIds,
            User user,
            long chatId) {
        super(NotificationType.NEW_USER, targetUserIds);
        this.changedUserPayload = new ChangedUserPayload(user, chatId);
    }
}
