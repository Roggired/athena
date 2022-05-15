package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class ViewMessageNotification extends Notification {
    private final Payload payload;


    public ViewMessageNotification(
            List<Long> targetUserIds,
            List<Long> messageIds,
            long viewerId
    ) {
        super(NotificationType.VIEWED_MESSAGES, targetUserIds);
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
