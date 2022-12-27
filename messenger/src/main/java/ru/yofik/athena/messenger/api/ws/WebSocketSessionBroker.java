package ru.yofik.athena.messenger.api.ws;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.ApplicationScope;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Component
@ApplicationScope
@Log4j2
public class WebSocketSessionBroker {
    private final Map<Long, WebSocketSession> subscriptions = new HashMap<>();
    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public WebSocketSessionBroker(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    public synchronized void registerSession(
            Long userId,
            WebSocketSession webSocketSession
    ) {
        subscriptions.put(userId, webSocketSession);
    }

    public synchronized void removeSession(
            Long userId
    ) {
        subscriptions.remove(userId);
    }

    public synchronized void sendToSession(
            Long userId,
            AthenaWSMessage athenaWSMessage
    ) {
        var session = subscriptions.get(userId);

        if (session != null) {
            threadPoolTaskExecutor.execute(() -> {
                try {
                    session.sendMessage(
                            new TextMessage(
                                    new Gson().toJson(athenaWSMessage)
                            )
                    );
                } catch (IOException e) {
                    log.fatal("Can't send message", e);
                }
            });
        }
    }

    public synchronized boolean isSubscribed(Long userId) {
        return subscriptions.containsKey(userId);
    }
}
