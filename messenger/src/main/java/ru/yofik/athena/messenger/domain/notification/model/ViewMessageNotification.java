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
public class ViewMessageNotification extends ChatNotification {
    private final Payload payload;


    public ViewMessageNotification(
            Chat chat,
            List<Long> messageIds,
            long viewerId
    ) {
        super(NotificationType.VIEWED_MESSAGES, chat);
        this.payload = new Payload(messageIds, viewerId);
    }

    public static class Payload {
        private final List<Long> messageIds;
        private final long viewerId;

        public Payload(List<Long> messageIds, long viewerId) {
            this.messageIds = messageIds;
            this.viewerId = viewerId;
        }
    }
}
