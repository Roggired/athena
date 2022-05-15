package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public class NewInvitationNotification extends Notification {
    private final Payload payload;


    public NewInvitationNotification(
            List<Long> targetUserIds,
            String id,
            User sender,
            Chat chat
    ) {
        super(NotificationType.NEW_INVITATION, targetUserIds);
        this.payload = new Payload(id, sender, chat);
    }

    @AllArgsConstructor
    @Getter
    public static class Payload {
        private final String id;
        private final User user;
        private final Chat chat;
    }
}
