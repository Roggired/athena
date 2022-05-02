package ru.yofik.athena.messenger.context.chat.model;

import lombok.*;
import ru.yofik.athena.messenger.context.chat.view.NotificationView;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Notification {
    private final Type type;

    private final Message message;


    public NotificationView toView() {
        return new NotificationView(
                type.toString(),
                message.toView()
        );
    }


    public enum Type {
        NEW_MESSAGE,
        UPDATED_MESSAGE,
        DELETED_MESSAGES
    }
}
