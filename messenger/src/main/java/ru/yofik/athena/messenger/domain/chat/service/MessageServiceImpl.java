package ru.yofik.athena.messenger.domain.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.comparator.Comparators;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.exception.InvalidDataException;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.api.http.chat.request.SendMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.domain.notification.model.*;
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
        var chat = chatService.getById(request.chatId);
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
    public void deleteMessages(long chatId, DeleteMessagesRequest request, boolean isGlobal) {
        var chat = chatService.getById(chatId);
        var currentUser = userService.getCurrentUser();
        var messages = messageRepository.getAllById(request.ids)
                .stream()
                .filter(message -> {
                    if (message.getOwningUserIds().contains(currentUser.getId())) {
                        return true;
                    }

                    throw new ResourceNotFoundException();
                })
                .collect(Collectors.toList());

        if (messages.isEmpty()) {
            return;
        }

        Notification notification;

        if (isGlobal) {
            var deleteMessagesIds = deleteGlobally(messages, currentUser);

            notification = new DeletedMessagesNotification(
                    getUserIdsToBeNotified(chat),
                    deleteMessagesIds
            );
        } else {
            var deleteMessagesIds = deleteLocally(messages, currentUser);

            notification = new DeletedMessagesNotification(
                    List.of(currentUser.getId()),
                    deleteMessagesIds
            );
        }

        notificationService.sendNotification(notification);
    }


    private List<Long> deleteGlobally(List<Message> messages, User currentUser) {
        var messageIdsToDelete = messages.stream()
                .filter(message -> {
                    if (message.getSenderId() == currentUser.getId()) {
                        return true;
                    }

                    throw new InvalidDataException("You can't globally delete a message which you haven't sent");
                })
                .map(Message::getId)
                .collect(Collectors.toList());
        messageRepository.deleteAllById(messageIdsToDelete);
        return messageIdsToDelete;
    }

    private List<Long> deleteLocally(List<Message> messages, User currentUser) {
        messages.forEach(message -> message.getOwningUserIds().remove(currentUser.getId()));
        messageRepository.saveAll(messages);

        deleteMessageIfNoAssociatedUser(messages);

        return messages.stream()
                .map(Message::getId)
                .collect(Collectors.toList());
    }

    private List<Long> getUserIdsToBeNotified(Chat chat) {
        return chat.getUsers()
                .stream()
                .map(User::getId)
                .collect(Collectors.toList());
    }

    private List<Long> getUserIdsToBeNotified(Chat chat, Message targetMessage) {
        return chat.getUsers()
                .stream()
                .map(User::getId)
                .filter(userId -> targetMessage.getOwningUserIds().contains(userId))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void updateMessage(long chatId, long messageId, UpdateMessageRequest request) {
        var message = messageRepository.getById(messageId);
        var chat = chatService.getById(chatId);

        var updatedMessage = new Message(
                message.getId(),
                request.text,
                message.getSenderId(),
                message.getChatId(),
                message.getCreationDate(),
                Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getOwningUserIds(),
                message.getViewedByUserIds()
        );

        messageRepository.save(message);

        notificationService.sendNotification(
                new UpdateMessageNotification(
                        getUserIdsToBeNotified(chat, message),
                        updatedMessage
                )
        );
    }

    private void deleteMessageIfNoAssociatedUser(List<Message> messages) {
        messageRepository.deleteAllById(
                messages.stream()
                        .filter(message -> message.getOwningUserIds().isEmpty())
                        .map(Message::getId)
                        .collect(Collectors.toList())
        );
    }

    @Override
    public Page<Message> getPageFor(Chat chat, Page.Meta pageMeta) {
        return messageRepository.getPageByChatIdAndOwningUserId(
                pageMeta,
                chat.getId(),
                userService.getCurrentUser().getId()
        ).sort((a, b) -> Comparators.comparable().compare(a.getCreationDate(), b.getCreationDate()));
    }

    @Override
    public Message getLastFor(Chat chat) {
        var pageMeta = new Page.Meta(0, 1);
        return getPageFor(chat, pageMeta).stream().findFirst().orElse(null);
    }

    @Override
    public void deleteMessagesByChatId(long chatId) {
        messageRepository.deleteAllByChatId(chatId);
    }

    @Override
    public void viewMessage(List<Long> messageIds) {
        var messages = messageRepository.getAllById(messageIds);
        var chat = chatService.getById(messages.get(0).getChatId());
        var viewer = userService.getCurrentUser();
        messages.forEach(message -> message.getViewedByUserIds().add(viewer.getId()));
        messageRepository.saveAll(messages);

        notificationService.sendNotification(new ViewMessageNotification(
                getUserIdsToBeNotified(chat),
                messageIds,
                viewer.getId()
        ));
    }
}
