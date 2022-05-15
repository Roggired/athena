package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.DeletedTopicNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

import java.util.List;

@Component
public class DeletedTopicNotificationViewMapper implements ConversionServiceConfig.Mapper<DeletedTopicNotification, NotificationView<List<Long>>> {
    @Override
    public NotificationView<List<Long>> convert(DeletedTopicNotification notification) {
        return new NotificationView<>(
                notification.getType().toString(),
                notification.getDeletedTopicIds()
        );
    }
}
