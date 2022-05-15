package ru.yofik.athena.messenger.domain.notification.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
public abstract class ChatNotification extends Notification {
    private final long chatId;


    public ChatNotification(NotificationType type, Chat chat) {
        super(type, chat.getUsers().stream().map(User::getId).collect(Collectors.toList()));
        this.chatId = chat.getId();
    }

    public ChatNotification(NotificationType type, Chat chat, Message targetMessage) {
        super(
                type,
                chat.getUsers()
                        .stream()
                        .map(User::getId)
                        .filter(userId -> targetMessage.getOwningUserIds().contains(userId))
                        .collect(Collectors.toList())
        );
        this.chatId = chat.getId();
    }
}
