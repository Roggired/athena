package ru.yofik.athena.messenger.context.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.context.chat.api.request.CreateChatRequest;
import ru.yofik.athena.messenger.context.chat.dto.ChatJpaDto;
import ru.yofik.athena.messenger.context.chat.factory.ChatFactory;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.chat.repository.ChatRepository;
import ru.yofik.athena.messenger.context.chat.view.ChatFullView;
import ru.yofik.athena.messenger.context.chat.view.ChatView;
import ru.yofik.athena.messenger.context.user.integration.UserApi;
import ru.yofik.athena.messenger.context.user.model.User;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class ChatServiceImpl extends AbstractService implements ChatService {
    private final UserApi userApi;
    private final ChatFactory chatFactory;
    private final ChatRepository chatRepository;

    public ChatServiceImpl(
            UserApi userApi, ChatFactory chatFactory,
            ChatRepository chatRepository
    ) {
        this.userApi = userApi;
        this.chatFactory = chatFactory;
        this.chatRepository = chatRepository;
    }

    @Override
    public ChatView create(CreateChatRequest request) {
        var user = getCurrentUser();
        var chat = chatFactory.create(request.name);

        chat.addUser(user);
        request.users
                .stream()
                .filter(el -> !Objects.equals(el, user.getId()))
                .map(el -> userApi.getUser(el, clientToken))
                .forEach(chat::addUser);

        return chatFactory.from(
                chatRepository.save(
                        chatFactory.to(chat)
                ),
                clientToken
        ).toView();
    }

    @Override
    public void delete(long id) {
        var chatJpaDto = getChatJpaDto(id);
        chatRepository.delete(chatJpaDto);
    }

    @Override
    public Chat get(long id) {
        return getChat(id);
    }

    @Override
    public ChatFullView getFull(long id) {
        return getChat(id).toFullView();
    }

    @Override
    public List<ChatView> getAll() {
        return chatRepository.findAll()
                .stream()
                .map(el -> chatFactory.from(el, clientToken))
                .map(Chat::toView)
                .collect(Collectors.toList());
    }

    private Chat getChat(long id) {
        var chatJpaDto = getChatJpaDto(id);
        return chatFactory.from(chatJpaDto, clientToken);
    }

    private ChatJpaDto getChatJpaDto(long id) {
        var chat = chatRepository.findById(id);
        if (chat.isEmpty()) {
            log.warn("No such chat: " + id);
            throw new ResourceNotFoundException();
        }

        return chat.get();
    }
}
