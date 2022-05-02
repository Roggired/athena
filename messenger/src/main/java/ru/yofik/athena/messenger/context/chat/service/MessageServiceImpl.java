package ru.yofik.athena.messenger.context.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.context.chat.api.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.context.chat.api.request.SendMessageRequest;
import ru.yofik.athena.messenger.context.chat.api.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.context.chat.dto.MessageJpaDto;
import ru.yofik.athena.messenger.context.chat.factory.ChatFactory;
import ru.yofik.athena.messenger.context.chat.factory.MessageFactory;
import ru.yofik.athena.messenger.context.chat.factory.NotificationFactory;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.chat.model.Message;
import ru.yofik.athena.messenger.context.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.infrastructure.wsApi.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class MessageServiceImpl extends AbstractService implements MessageService {
    private final MessageRepository messageRepository;
    private final MessageFactory messageFactory;
    private final ChatService chatService;
    private final ChatFactory chatFactory;
    private final WebSocketSessionBroker webSocketSessionBroker;
    private final NotificationFactory notificationFactory;

    public MessageServiceImpl(
            MessageRepository messageRepository,
            MessageFactory messageFactory,
            ChatService chatService,
            ChatFactory chatFactory,
            WebSocketSessionBroker webSocketSessionBroker,
            NotificationFactory notificationFactory) {
        this.messageRepository = messageRepository;
        this.messageFactory = messageFactory;
        this.chatService = chatService;
        this.chatFactory = chatFactory;
        this.webSocketSessionBroker = webSocketSessionBroker;
        this.notificationFactory = notificationFactory;
    }

    @Override
    public void sendMessage(SendMessageRequest request) {
        var user = getCurrentUser();
        var chat = chatService.getWithoutMessages(request.chatId);
        var message = messageFactory.create(request.text, user, chat);
        var messageJpaDto = messageRepository.save(
                messageFactory.to(
                        message,
                        chatFactory.to(
                                chat
                        )
                )
        );
        var athenaWSMessage = notificationFactory.newMessage(messageFactory.from(messageJpaDto));
        sendAthenaWSNotificationMessage(athenaWSMessage, chat);
    }

    @Override
    public void deleteMessage(long chatId, long messageId) {
        var chat = chatService.getWithoutMessages(chatId);
        var message = getMessage(messageId);
        messageRepository.deleteById(message.getId());
        var athenaWSMessage = notificationFactory.deletedMessages(List.of(messageId));
        sendAthenaWSNotificationMessage(athenaWSMessage, chat);
    }

    @Override
    public void deleteMessages(long chatId, DeleteMessagesRequest request) {
        var chat = chatService.getWithoutMessages(chatId);
        var messages = messageRepository.findAllById(request.ids);
        var messagesToDeleteIds = messages.stream()
                .map(MessageJpaDto::getId)
                .collect(Collectors.toList());
        messageRepository.deleteAllById(messagesToDeleteIds);
        var athenaWSMessage = notificationFactory.deletedMessages(messagesToDeleteIds);
        sendAthenaWSNotificationMessage(athenaWSMessage, chat);
    }

    @Override
    @Transactional
    public void updateMessage(long chatId, long messageId, UpdateMessageRequest request) {
        var message = messageFactory.from(
                messageRepository.findById(messageId)
                        .orElseThrow(ResourceNotFoundException::new)
        );

        var chat = chatService.getWithoutMessages(chatId);

        var updatedMessage = new Message(
                message.getId(),
                request.text,
                message.getSenderId(),
                message.getChatId(),
                message.getCreationDate(),
                Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime()
        );

        messageRepository.save(
                messageFactory.to(
                        updatedMessage,
                        chatFactory.to(
                                chat
                        )
                )
        );

        var athenaWSMessage = notificationFactory.updatedMessage(updatedMessage);
        sendAthenaWSNotificationMessage(athenaWSMessage, chat);
    }

    private Message getMessage(long id) {
        return messageFactory.from(
                messageRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    private void sendAthenaWSNotificationMessage(
            AthenaWSMessage athenaWSMessage,
            Chat chat
    ) {
        for (var targetUser : chat.getUsers()) {
            webSocketSessionBroker.sendToSession(
                    new NotificationSubscriptionKey(targetUser.getId()),
                    WebSocketSubscriptionType.NOTIFICATION,
                    athenaWSMessage
            );
        }
    }
}
