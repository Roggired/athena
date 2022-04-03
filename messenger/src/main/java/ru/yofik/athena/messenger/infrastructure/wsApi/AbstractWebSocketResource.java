package ru.yofik.athena.messenger.infrastructure.wsApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ru.yofik.athena.messenger.infrastructure.SpringContext;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Log4j2
public abstract class AbstractWebSocketResource extends AbstractWebSocketHandler {
    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(AthenaWSMessage.class, (JsonDeserializer<AthenaWSMessage>) (json, typeOfT, context) -> {
                var jsonObject = json.getAsJsonObject();
                var command = AthenaWSCommand.valueOf(jsonObject.get("command").getAsString());
                var argumentJson = jsonObject.get("argument");
                String argument = "";
                if (argumentJson != null) {
                    argument = jsonObject.get("argument").getAsString();
                }
                return new AthenaWSMessage(command, argument);
            })
            .create();

    protected WebSocketSessionBroker webSocketSessionBroker;
    private final ConcurrentMap<String, Pair<SubscriptionKey, WebSocketSubscriptionType>> sessions = new ConcurrentHashMap<>();

    public AbstractWebSocketResource() {
        webSocketSessionBroker = SpringContext.getBean(WebSocketSessionBroker.class);
    }

    protected abstract void handleAthenaWSMessage(WebSocketSession session, AthenaWSMessage message) throws Exception;

    protected void subscribe(WebSocketSession session, SubscriptionKey key, WebSocketSubscriptionType subscriptionType) {
        sessions.put(session.getId(), Pair.of(key, subscriptionType));
        webSocketSessionBroker.registerSession(key, subscriptionType, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        var athenaWSMessage = GSON.fromJson(message.getPayload(), AthenaWSMessage.class);
        handleAthenaWSMessage(session, athenaWSMessage);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) throws Exception {
        log.fatal("Binary messages cannot be handled by WebSocketResources");
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.fatal("Pong messages cannot be handled by WebSocketResources");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        var pair = sessions.get(session.getId());
        log.fatal("Transport error has occurred for session: " + ((pair == null) ? session.getId() : " key: " + pair.getLeft() + " subscriptionType: " + pair.getRight()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        var pair = sessions.get(session.getId());

        if (pair != null) {
            webSocketSessionBroker.removeSession(pair.getLeft(), pair.getRight());
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
