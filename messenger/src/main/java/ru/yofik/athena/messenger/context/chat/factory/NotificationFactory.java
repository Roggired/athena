package ru.yofik.athena.messenger.context.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.context.chat.model.Message;
import ru.yofik.athena.messenger.context.chat.model.Notification;
import ru.yofik.athena.messenger.context.chat.view.DeletedMessagesNotificationView;
import ru.yofik.athena.messenger.infrastructure.wsApi.AthenaWSCommand;
import ru.yofik.athena.messenger.infrastructure.wsApi.AthenaWSMessage;

import java.util.List;

@Component
public class NotificationFactory {
    public AthenaWSMessage newMessage(Message message) {
        return createAthenaWSNotificationMessage(Notification.Type.NEW_MESSAGE, message);
    }

    public AthenaWSMessage updatedMessage(Message message) {
        return createAthenaWSNotificationMessage(Notification.Type.UPDATED_MESSAGE, message);
    }

    public AthenaWSMessage deletedMessages(List<Long> messageIds) {
        return new AthenaWSMessage(
                AthenaWSCommand.RECEIVE_NOTIFICATION,
                new DeletedMessagesNotificationView(
                        Notification.Type.DELETED_MESSAGES.toString(),
                        messageIds
                )
        );
    }

    private AthenaWSMessage createAthenaWSNotificationMessage(
            Notification.Type type,
            Message message
    ) {
        return new AthenaWSMessage(
                AthenaWSCommand.RECEIVE_NOTIFICATION,
                new Notification(
                        type,
                        message
                ).toView()
        );
    }
}
