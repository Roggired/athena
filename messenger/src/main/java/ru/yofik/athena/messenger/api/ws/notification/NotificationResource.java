package ru.yofik.athena.messenger.api.ws.notification;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;
import ru.yofik.athena.messenger.api.ws.AbstractWebSocketResource;
import ru.yofik.athena.messenger.api.ws.AthenaWSMessage;
import ru.yofik.athena.messenger.api.ws.NotificationSubscriptionKey;
import ru.yofik.athena.messenger.api.ws.WebSocketSubscriptionType;
import ru.yofik.athena.messenger.domain.user.model.User;

@Log4j2
public class NotificationResource extends AbstractWebSocketResource {
    @Override
    protected void handleAthenaWSMessage(WebSocketSession session, AthenaWSMessage message) throws Exception {
        log.info("Got new WS messages: " + message);
        var authentication = (Authentication) session.getPrincipal();
        Assert.notNull(authentication, "ChatResource cannot get user!");
        var user = (User) authentication.getDetails();
        subscribe(session, new NotificationSubscriptionKey(user.getId()), WebSocketSubscriptionType.NOTIFICATION);
    }
}
