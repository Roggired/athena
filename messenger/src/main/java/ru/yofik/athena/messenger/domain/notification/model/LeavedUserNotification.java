package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class LeavedUserNotification extends Notification {
    private final ChangedUserPayload changedUserPayload;


    public LeavedUserNotification(
            List<Long> targetUserIds,
            User user,
            long chatId
    ) {
        super(NotificationType.LEAVED_USER, targetUserIds);
        this.changedUserPayload = new ChangedUserPayload(user, chatId);
    }
}
