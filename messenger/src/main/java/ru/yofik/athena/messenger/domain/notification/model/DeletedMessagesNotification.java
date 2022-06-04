package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class DeletedMessagesNotification extends Notification {
    private final List<Long> deletedMessages;
    private final long targetChatId;

    public DeletedMessagesNotification(long targetChatId, List<Long> targetUserIds, List<Long> deletedMessages) {
        super(NotificationType.DELETED_MESSAGES, targetUserIds);
        this.targetChatId = targetChatId;
        this.deletedMessages = deletedMessages;
    }

    public DeletedMessagesNotification(
            Chat chat,
            List<Long> deletedMessages
    ) {
        super(NotificationType.DELETED_MESSAGES, chat.getUsers().stream().map(User::getId).collect(Collectors.toList()));
        this.targetChatId = chat.getId();
        this.deletedMessages = deletedMessages;
    }
}
