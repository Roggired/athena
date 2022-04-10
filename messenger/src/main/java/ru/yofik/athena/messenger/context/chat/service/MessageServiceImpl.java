package ru.yofik.athena.messenger.context.chat.service;

import com.google.gson.Gson;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.context.chat.api.request.SendMessageRequest;
import ru.yofik.athena.messenger.context.chat.factory.ChatFactory;
import ru.yofik.athena.messenger.context.chat.factory.MessageFactory;
import ru.yofik.athena.messenger.context.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.infrastructure.wsApi.*;

@Service
@RequestScope
@Log4j2
public class MessageServiceImpl extends AbstractService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageFactory messageFactory;
    private final ChatService chatService;
    private final ChatFactory chatFactory;
    private final WebSocketSessionBroker webSocketSessionBroker;

    public MessageServiceImpl(
            MessageRepository messageRepository,
            MessageFactory messageFactory,
            ChatService chatService,
            ChatFactory chatFactory,
            WebSocketSessionBroker webSocketSessionBroker
    ) {
        this.messageRepository = messageRepository;
        this.messageFactory = messageFactory;
        this.chatService = chatService;
        this.chatFactory = chatFactory;
        this.webSocketSessionBroker = webSocketSessionBroker;
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        var user = getCurrentUser();
        var chat = chatService.get(request.chatId);
        var message = messageFactory.create(request.text, user, chat);
        var messageJpaDto = messageRepository.save(
                messageFactory.to(
                        message,
                        chatFactory.to(
                                chat
                        )
                )
        );
        message = messageFactory.from(messageJpaDto);

        var athenaWSMessage = new AthenaWSMessage(
                AthenaWSCommand.RECEIVE_NOTIFICATION,
                message.toView()
        );

        for (var targetUser : chat.getUsers()) {
            webSocketSessionBroker.sendToSession(
                    new NotificationSubscriptionKey(targetUser.getId()),
                    WebSocketSubscriptionType.NOTIFICATION,
                    athenaWSMessage
            );
        }
    }
}
