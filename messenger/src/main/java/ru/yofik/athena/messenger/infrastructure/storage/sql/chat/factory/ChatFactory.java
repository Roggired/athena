package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.service.UserService;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.ChatEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class ChatFactory {
    private final UserService userService;

    public ChatFactory(UserService userService) {
        this.userService = userService;
    }

    public Chat fromEntity(ChatEntity chatEntity) {
        return new Chat(
                chatEntity.getId(),
                chatEntity.getName(),
                chatEntity.getUserIds()
                        .stream()
                        .map(userService::getUser)
                        .collect(Collectors.toList())
        );
    }

    public ChatEntity toEntity(Chat chat) {
        return new ChatEntity(
                chat.getId(),
                chat.getName(),
                chat.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
    }
}
