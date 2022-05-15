package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.DeletedMessagesNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

import java.util.List;

@Component
public class DeletedMessagesNotificationViewMapper implements ConversionServiceConfig.Mapper<DeletedMessagesNotification, NotificationView<List<Long>>> {
    @Override
    public NotificationView<List<Long>> convert(DeletedMessagesNotification notification) {
        return new NotificationView<>(
                notification.getType().name(),
                notification.getTargetChatId(),
                notification.getDeletedMessages()
        );
    }
}
