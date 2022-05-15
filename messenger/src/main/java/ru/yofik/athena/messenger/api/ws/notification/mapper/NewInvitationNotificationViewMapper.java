package ru.yofik.athena.messenger.api.ws.notification.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.mapper.ChatViewMapper;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.api.http.user.mapper.UserViewMapper;
import ru.yofik.athena.messenger.api.http.user.view.UserView;
import ru.yofik.athena.messenger.api.ws.notification.view.NotificationView;
import ru.yofik.athena.messenger.domain.notification.model.NewInvitationNotification;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class NewInvitationNotificationViewMapper implements ConversionServiceConfig.Mapper<NewInvitationNotification, NotificationView<NewInvitationNotificationViewMapper.PayloadView>> {
    @Override
    public NotificationView<PayloadView> convert(NewInvitationNotification notification) {
        var userViewMapper = new UserViewMapper();
        var chatViewMapper = new ChatViewMapper();

        return new NotificationView<>(
                notification.getType().toString(),
                null,
                new PayloadView(
                        notification.getPayload().getId(),
                        userViewMapper.convert(notification.getPayload().getUser()),
                        chatViewMapper.convert(notification.getPayload().getChat())
                )
        );
    }

    @AllArgsConstructor
    public static class PayloadView {
        public final String id;
        public final UserView sender;
        public final ChatView chat;
    }
}
