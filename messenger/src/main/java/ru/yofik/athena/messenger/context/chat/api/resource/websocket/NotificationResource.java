package ru.yofik.athena.messenger.context.chat.api.resource.websocket;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;
import ru.yofik.athena.messenger.context.user.model.User;
import ru.yofik.athena.messenger.infrastructure.wsApi.AbstractWebSocketResource;
import ru.yofik.athena.messenger.infrastructure.wsApi.AthenaWSMessage;
import ru.yofik.athena.messenger.infrastructure.wsApi.NotificationSubscriptionKey;
import ru.yofik.athena.messenger.infrastructure.wsApi.WebSocketSubscriptionType;

@Log4j2
public class NotificationResource extends AbstractWebSocketResource {
    @Override
    protected void handleAthenaWSMessage(WebSocketSession session, AthenaWSMessage message) throws Exception {
        var authentication = (Authentication) session.getPrincipal();
        Assert.notNull(authentication, "ChatResource cannot get user!");
        var user = (User) authentication.getDetails();
        subscribe(session, new NotificationSubscriptionKey(user.getId()), WebSocketSubscriptionType.NOTIFICATION);
    }
}
