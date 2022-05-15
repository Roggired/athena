package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.mapper.MessageViewMapper;
import ru.yofik.athena.messenger.api.http.chat.view.MessageView;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.NewMessageNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class NewNotificationViewMapper implements ConversionServiceConfig.Mapper<NewMessageNotification, NotificationView<MessageView>> {
    @Override
    public NotificationView<MessageView> convert(NewMessageNotification notification) {
        var messageViewMapper = new MessageViewMapper();
        return new NotificationView<>(
                notification.getType().toString(),
                notification.getChatId(),
                messageViewMapper.convert(notification.getMessage())
        );
    }
}
