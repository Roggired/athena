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
public class UpdateMessageNotification extends Notification {
    private final Message message;

    public UpdateMessageNotification(List<Long> targetUserIds, Message message) {
        super(NotificationType.UPDATED_MESSAGE, targetUserIds);
        this.message = message;
    }
}
