package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.ViewMessageNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class ViewMessageNotificationViewMapper implements ConversionServiceConfig.Mapper<ViewMessageNotification, NotificationView<ViewMessageNotification.Payload>> {
    @Override
    public NotificationView<ViewMessageNotification.Payload> convert(ViewMessageNotification notification) {
        return new NotificationView<>(
                notification.getType().toString(),
                notification.getPayload()
        );
    }
}
