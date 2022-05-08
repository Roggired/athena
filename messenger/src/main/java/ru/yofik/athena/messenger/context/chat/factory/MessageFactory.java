package ru.yofik.athena.messenger.context.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.context.chat.dto.ChatJpaDto;
import ru.yofik.athena.messenger.context.chat.dto.MessageJpaDto;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.chat.model.Message;
import ru.yofik.athena.messenger.context.user.model.User;

import java.time.Instant;
import java.time.ZoneId;
import java.util.stream.Collectors;

@Component
public class MessageFactory {
    public Message create(String text, User sender, Chat chat) {
        var creationDate = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
        return new Message(
                0,
                text,
                sender.getId(),
                chat.getId(),
                creationDate,
                creationDate,
                chat.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
    }

    public Message from(MessageJpaDto messageJpaDto) {
        return new Message(
                messageJpaDto.getId(),
                messageJpaDto.getText(),
                messageJpaDto.getSenderId(),
                messageJpaDto.getChat().getId(),
                messageJpaDto.getCreationDate(),
                messageJpaDto.getModificationDate(),
                messageJpaDto.getOwningUserIds()
        );
    }

    public MessageJpaDto to(Message message, ChatJpaDto chatJpaDto) {
        return new MessageJpaDto(
                message.getId(),
                message.getText(),
                message.getSenderId(),
                chatJpaDto,
                message.getCreationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getModificationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getOwningUserIds()
        );
    }
}
