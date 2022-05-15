package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Message;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ChangedMessageNotification extends Notification {
    private final ChangedMessagePayload changedMessagePayload;

    public ChangedMessageNotification(NotificationType type, List<Long> targetUserIds, long userId, Message message) {
        super(type, targetUserIds);
        this.changedMessagePayload = new ChangedMessagePayload(userId, message);
    }
}
