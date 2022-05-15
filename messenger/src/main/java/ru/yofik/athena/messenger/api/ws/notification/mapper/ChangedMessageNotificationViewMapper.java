package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.ChangedMessagePayloadView;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.ChangedMessageNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class ChangedMessageNotificationViewMapper implements ConversionServiceConfig.Mapper<ChangedMessageNotification, NotificationView<ChangedMessagePayloadView>> {
    @Override
    public NotificationView<ChangedMessagePayloadView> convert(ChangedMessageNotification notification) {
        var changedMessagePayloadViewMapper = new ChangedMessagePayloadViewMapper();
        return new NotificationView<>(
                notification.getType().toString(),
                changedMessagePayloadViewMapper.convert(notification.getChangedMessagePayload())
        );
    }
}
