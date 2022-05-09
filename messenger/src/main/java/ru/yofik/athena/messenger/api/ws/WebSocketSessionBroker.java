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
    private Map<WebSocketSubscriptionType, Map<SubscriptionKey, WebSocketSession>> subscriptions;

    private final ThreadPoolTaskExecutor threadPoolTaskExecutor;

    public WebSocketSessionBroker(ThreadPoolTaskExecutor threadPoolTaskExecutor) {
        this.threadPoolTaskExecutor = threadPoolTaskExecutor;
    }

    @PostConstruct
    public void init() {
        subscriptions = new HashMap<>();
        Arrays.stream(WebSocketSubscriptionType.values())
                .forEach(subscriptionType -> subscriptions.put(subscriptionType, new HashMap<>()));
    }

    public synchronized void registerSession(
            SubscriptionKey subscriptionKey,
            WebSocketSubscriptionType subscriptionType,
            WebSocketSession webSocketSession
    ) {
        subscriptions.get(subscriptionType).put(subscriptionKey, webSocketSession);
    }

    public synchronized void removeSession(
            SubscriptionKey subscriptionKey,
            WebSocketSubscriptionType subscriptionType
    ) {
        subscriptions.get(subscriptionType).remove(subscriptionKey);
    }

    public synchronized void sendToSession(
            SubscriptionKey subscriptionKey,
            WebSocketSubscriptionType subscriptionType,
            AthenaWSMessage athenaWSMessage
    ) {
        var session = subscriptions.get(subscriptionType).get(subscriptionKey);

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
}
