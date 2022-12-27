package ru.yofik.athena.messenger.api.ws.notification;

import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.Assert;
import org.springframework.web.socket.WebSocketSession;
import ru.yofik.athena.common.security.AccessTokenAuthentication;
import ru.yofik.athena.messenger.api.ws.AbstractWebSocketResource;
import ru.yofik.athena.messenger.api.ws.AthenaWSMessage;
import ru.yofik.athena.messenger.api.ws.NotificationSubscriptionKey;
import ru.yofik.athena.messenger.api.ws.WebSocketSubscriptionType;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.entity.UserLastOnlineRecord;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository.CrudUserLastOnlineRecordRepository;

import java.time.Instant;
import java.time.ZoneId;

@Log4j2
public class NotificationResource extends AbstractWebSocketResource {
    private final CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository;
    private final UserRepository userRepository;

    public NotificationResource(CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository, UserRepository userRepository) {
        this.crudUserLastOnlineRecordRepository = crudUserLastOnlineRecordRepository;
        this.userRepository = userRepository;
    }

    @Override
    protected void handleAthenaWSMessage(WebSocketSession session, AthenaWSMessage message) throws Exception {
        log.info("Got new WS messages: " + message);
        var authentication = (AccessTokenAuthentication) session.getPrincipal();
        Assert.notNull(authentication, "ChatResource cannot get user!");
        var userId = (Long) authentication.getPrincipal();
        Assert.notNull(userId, "ChatResource cannot get user!");

        var securityContext = SecurityContextHolder.getContext();
        if (securityContext == null) {
            securityContext = new SecurityContextImpl();
        }
        securityContext.setAuthentication(authentication);

        var user = userRepository.getUser(userId);
        subscribe(session, new NotificationSubscriptionKey(user.getId()), WebSocketSubscriptionType.NOTIFICATION);

        var userLastOnlineRecord = crudUserLastOnlineRecordRepository.findByUserId(user.getId());
        var lastOnlineTime = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();

        if (userLastOnlineRecord.isPresent()) {
            userLastOnlineRecord.get().setLastOnlineTime(lastOnlineTime);
            crudUserLastOnlineRecordRepository.save(userLastOnlineRecord.get());
        } else {
            var newUserLastOnlineRecord = new UserLastOnlineRecord();
            newUserLastOnlineRecord.setUserId(user.getId());
            newUserLastOnlineRecord.setLastOnlineTime(lastOnlineTime);
            crudUserLastOnlineRecordRepository.save(newUserLastOnlineRecord);
        }
    }
}
