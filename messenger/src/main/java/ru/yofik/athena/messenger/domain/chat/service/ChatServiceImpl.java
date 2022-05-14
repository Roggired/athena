package ru.yofik.athena.messenger.domain.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.chat.request.CreateChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.repository.ChatRepository;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final MessageService messageService;

    public ChatServiceImpl(
            UserService userService,
            ChatRepository chatRepository,
            MessageService messageService) {
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.messageService = messageService;
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
        messageService.deleteMessagesByChatId(chat.getId());
        chatRepository.delete(chat.getId());
    }

    @Override
    public Chat getById(long id) {
        var chat = chatRepository.getById(id)
                .chooseChatNameFor(userService.getCurrentUser());
        chat.setLastMessage(messageService.getLastFor(chat));
        return chat;
    }

    @Override
    public Page<Chat> getPageForCurrentUser(Page.Meta pageMeta) {
        var page = chatRepository.getPage(pageMeta);
        return page.map(chat -> {
            chat.chooseChatNameFor(userService.getCurrentUser());
            chat.setLastMessage(messageService.getLastFor(chat));
            return chat;
        });
    }
}
