package ru.yofik.athena.messenger.domain.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.api.http.chat.request.SendMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.domain.notification.model.DeletedMessagesNotification;
import ru.yofik.athena.messenger.domain.notification.model.NewMessageNotification;
import ru.yofik.athena.messenger.domain.notification.model.Notification;
import ru.yofik.athena.messenger.domain.notification.model.UpdateMessageNotification;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import java.time.Instant;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class MessageServiceImpl implements MessageService {
    private final ChatService chatService;
    private final NotificationService notificationService;
    private final UserService userService;
    private final MessageRepository messageRepository;

    public MessageServiceImpl(
            ChatService chatService,
            NotificationService notificationService,
            UserService userService,
            MessageRepository messageRepository) {
        this.chatService = chatService;
        this.notificationService = notificationService;
        this.userService = userService;
        this.messageRepository = messageRepository;
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void sendMessage(SendMessageRequest request) {
        var user = userService.getCurrentUser();
        var chat = chatService.getWithoutMessages(request.chatId);
        var message = messageRepository.save(
                Message.newMessage(request.text, user, chat)
        );
        notificationService.sendNotification(
                new NewMessageNotification(
                        getUserIdsToBeNotified(chat),
                        message
                )
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void deleteMessage(long chatId, long messageId, boolean isGlobal) {
        var chat = chatService.getWithoutMessages(chatId);
        var message = messageRepository.getById(messageId);
        var currentUser = userService.getCurrentUser();

        if (!message.getOwningUserIds().contains(currentUser.getId())) {
            throw new ResourceNotFoundException();
        }

        Notification notification;

        if (isGlobal) {
            messageRepository.deleteById(message.getId());

            notification = new DeletedMessagesNotification(
                    getUserIdsToBeNotified(chat),
                    List.of(messageId)
            );
        } else {
            var targetUser = userService.getCurrentUser();
            message.getOwningUserIds().remove(targetUser.getId());
            messageRepository.save(message);

            deleteMessageIfNoAssociatedUser(message);

            notification = new DeletedMessagesNotification(
                    List.of(targetUser.getId()),
                    List.of(messageId)
            );
        }

        notificationService.sendNotification(notification);
    }

    private List<Long> getUserIdsToBeNotified(Chat chat) {
        return chat.getUsers()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void deleteMessages(long chatId, DeleteMessagesRequest request, boolean isGlobal) {
        var chat = chatService.getWithoutMessages(chatId);
        var messages = messageRepository.getAllById(request.ids);
        var currentUser = userService.getCurrentUser();
        var messagesToDeleteIds = messages.stream()
                .filter(message -> message.getOwningUserIds().contains(currentUser.getId()))
                .map(Message::getId)
                .collect(Collectors.toList());

        if (messagesToDeleteIds.isEmpty()) {
            return;
        }

        Notification notification;

        if (isGlobal) {
            messageRepository.deleteAllById(messagesToDeleteIds);

            notification = new DeletedMessagesNotification(
                    getUserIdsToBeNotified(chat),
                    messagesToDeleteIds
            );
        } else {
            var targetUser = userService.getCurrentUser();
            messages.forEach(message -> message.getOwningUserIds().remove(targetUser.getId()));
            messageRepository.saveAll(messages);

            deleteMessageIfNoAssociatedUser(messages);

            notification = new DeletedMessagesNotification(
                    List.of(targetUser.getId()),
                    messagesToDeleteIds
            );
        }

        notificationService.sendNotification(notification);
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void updateMessage(long chatId, long messageId, UpdateMessageRequest request) {
        var message = messageRepository.getById(messageId);
        var chat = chatService.getWithoutMessages(chatId);

        var updatedMessage = new Message(
                message.getId(),
                request.text,
                message.getSenderId(),
                message.getChatId(),
                message.getCreationDate(),
                Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getOwningUserIds()
        );

        messageRepository.save(message);

        notificationService.sendNotification(
                new UpdateMessageNotification(
                        getUserIdsToBeNotified(chat),
                        updatedMessage
                )
        );
    }

    private void deleteMessageIfNoAssociatedUser(Message message) {
        if (message.getOwningUserIds().isEmpty()) {
            messageRepository.deleteById(message.getId());
        }
    }

    private void deleteMessageIfNoAssociatedUser(List<Message> messages) {
        messageRepository.deleteAllById(
                messages.stream()
                        .filter(message -> message.getOwningUserIds().isEmpty())
                        .map(Message::getId)
                        .collect(Collectors.toList())
        );
    }
}
