package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Chat;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DeletedTopicNotification extends ChatNotification {
    private final List<Long> deletedTopicIds;

    public DeletedTopicNotification(
            Chat chat,
            List<Long> deletedTopicIds
    ) {
        super(NotificationType.DELETED_TOPICS, chat);
        this.deletedTopicIds = deletedTopicIds;
    }
}
