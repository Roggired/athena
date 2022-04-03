package ru.yofik.athena.messenger.context.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.context.chat.dto.ChatJpaDto;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.user.integration.UserApi;
import ru.yofik.athena.messenger.context.user.model.User;

import java.util.ArrayList;
import java.util.stream.Collectors;

@Component
public class ChatFactory {
    private final UserApi userApi;
    private final MessageFactory messageFactory;

    public ChatFactory(UserApi userApi, MessageFactory messageFactory) {
        this.userApi = userApi;
        this.messageFactory = messageFactory;
    }

    public Chat create(String name) {
        return new Chat(
                name
        );
    }

    public Chat from(ChatJpaDto chatJpaDto, char[] clientToken) {
        return new Chat(
                chatJpaDto.getId(),
                chatJpaDto.getName(),
                chatJpaDto.getUserIds()
                        .stream()
                        .map(id -> userApi.getUser(id, clientToken))
                        .collect(Collectors.toList()),
                chatJpaDto.getMessages()
                        .stream()
                        .map(messageFactory::from)
                        .collect(Collectors.toList())
        );
    }

    public ChatJpaDto to(Chat chat) {
        var chatJpaDto = new ChatJpaDto(
                chat.getId(),
                chat.getName(),
                chat.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList()),
                new ArrayList<>()
        );
        chat.getMessages()
                .forEach(message -> chatJpaDto.getMessages().add(messageFactory.to(message, chatJpaDto)));
        return chatJpaDto;
    }
}
