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
public class DeletedTopicNotification extends Notification {
    private final List<Long> deletedTopicIds;

    public DeletedTopicNotification(
            List<Long> targetUserIds,
            List<Long> deletedTopicIds
    ) {
        super(NotificationType.DELETED_TOPICS, targetUserIds);
        this.deletedTopicIds = deletedTopicIds;
    }
}
