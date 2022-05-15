package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.ChangedUserPayloadView;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.NewUserNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class NewUserNotificationViewMapper implements ConversionServiceConfig.Mapper<NewUserNotification, NotificationView<ChangedUserPayloadView>> {
    @Override
    public NotificationView<ChangedUserPayloadView> convert(NewUserNotification notification) {
        var changedUserPayloadViewMapper = new ChangedUserPayloadViewMapper();
        return new NotificationView<>(
                notification.getType().toString(),
                notification.getChatId(),
                changedUserPayloadViewMapper.convert(notification.getChangedUserPayload())
        );
    }
}
