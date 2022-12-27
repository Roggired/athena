package ru.yofik.athena.messenger.api.ws;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import lombok.extern.log4j.Log4j2;
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
    private final ConcurrentMap<String, Long> userSessions = new ConcurrentHashMap<>();

    public AbstractWebSocketResource() {
        webSocketSessionBroker = SpringContext.getBean(WebSocketSessionBroker.class);
    }

    protected abstract void handleAthenaWSMessage(WebSocketSession session, AthenaWSMessage message) throws Exception;

    protected void subscribe(Long userId, WebSocketSession session) {
        userSessions.put(session.getId(), userId);
        webSocketSessionBroker.registerSession(userId, session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        log.info("Handle text message for session: " + session.getId());
        var athenaWSMessage = GSON.fromJson(message.getPayload(), AthenaWSMessage.class);
        handleAthenaWSMessage(session, athenaWSMessage);
    }

    @Override
    protected void handleBinaryMessage(WebSocketSession session, BinaryMessage message) {
        log.fatal("Binary messages cannot be handled by WebSocketResources");
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) {
        log.fatal("Pong messages cannot be handled by WebSocketResources");
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        var userId = userSessions.get(session.getId());
        log.fatal("Transport error has occurred for session: " + ((userId == null) ? session.getId() : " userId: " + userId));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        var userId = userSessions.get(session.getId());

        if (userId != null) {
            webSocketSessionBroker.removeSession(userId);
        }
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }
}
