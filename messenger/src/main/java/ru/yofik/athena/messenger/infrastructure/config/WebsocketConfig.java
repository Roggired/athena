package ru.yofik.athena.messenger.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ru.yofik.athena.messenger.api.ws.notification.NotificationResource;
import ru.yofik.athena.messenger.domain.user.repository.UserRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.user.repository.CrudUserLastOnlineRecordRepository;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebsocketConfig implements WebSocketConfigurer {
    private final CrudUserLastOnlineRecordRepository crudUserLastOnlineRecordRepository;
    private final UserRepository userRepository;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new NotificationResource(crudUserLastOnlineRecordRepository, userRepository), "/ws-api/v1/notifications");
    }
}
