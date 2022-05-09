package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DeletedMessagesNotification extends Notification {
    private final List<Long> deletedMessages;

    public DeletedMessagesNotification(List<Long> targetUserIds, List<Long> deletedMessages) {
        super(NotificationType.DELETED_MESSAGES, targetUserIds);
        this.deletedMessages = deletedMessages;
    }
}
