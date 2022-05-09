package ru.yofik.athena.messenger.domain.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.api.http.chat.request.CreateChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.repository.ChatRepository;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    public ChatServiceImpl(
            UserService userService,
            ChatRepository chatRepository,
            MessageRepository messageRepository
    ) {
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.messageRepository = messageRepository;
    }

    @Override
    public Chat create(CreateChatRequest request) {
        var user = userService.getCurrentUser();
        var chat = new Chat(request.name);

        chat.addUser(user);
        request.users
                .stream()
                .filter(id -> !Objects.equals(id, user.getId()))
                .map(userService::getUser)
                .forEach(chat::addUser);

        return chatRepository.save(chat);
    }

    @Override
    public void delete(long id) {
        var chat = chatRepository.getById(id);
        chat.getMessages().forEach(message -> messageRepository.deleteById(message.getId()));
        chatRepository.delete(chat.getId());
    }

    @Override
    public Chat getWithoutMessages(long id) {
        return chatRepository.getWithoutMessagesById(id)
                .chooseChatNameFor(userService.getCurrentUser());
    }

    @Override
    public Chat getFull(long id) {
        var currentUser = userService.getCurrentUser();
        return chatRepository.getById(id)
                .hideMessagesForUser(currentUser)
                .sortMessages()
                .chooseChatNameFor(currentUser);
    }

    @Override
    public List<Chat> getAllForCurrentUser() {
        var currentUser = userService.getCurrentUser();
        return chatRepository.getAll()
                .stream()
                .peek(chat -> chat.hideMessagesForUser(currentUser)
                        .onlyLastMessage()
                        .chooseChatNameFor(currentUser)
                )
                .collect(Collectors.toList());
    }
}
