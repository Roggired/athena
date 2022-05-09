package ru.yofik.athena.messenger.api.ws;

import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.Notification;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;

import java.util.List;

@Component
public class AthenaWSNotifier implements NotificationService {
    private final WebSocketSessionBroker webSocketSessionBroker;
    private final ConversionService conversionService;

    public AthenaWSNotifier(
            WebSocketSessionBroker webSocketSessionBroker,
            ConversionService conversionService
    ) {
        this.webSocketSessionBroker = webSocketSessionBroker;
        this.conversionService = conversionService;
    }

    @Override
    public void sendNotification(Notification notification) {
        sendAthenaWSNotificationMessage(
                createAthenaWSNotificationMessage(notification),
                notification.getTargetUserIds()
        );
    }

    private AthenaWSMessage createAthenaWSNotificationMessage(
            Notification notification
    ) {
        return new AthenaWSMessage(
                AthenaWSCommand.RECEIVE_NOTIFICATION,
                conversionService.convert(
                        notification,
                        NotificationView.class
                )
        );
    }

    private void sendAthenaWSNotificationMessage(
            AthenaWSMessage athenaWSMessage,
            List<Long> targetUserIds
    ) {
        for (var targetUserId : targetUserIds) {
            webSocketSessionBroker.sendToSession(
                    new NotificationSubscriptionKey(targetUserId),
                    WebSocketSubscriptionType.NOTIFICATION,
                    athenaWSMessage
            );
        }
    }
}
