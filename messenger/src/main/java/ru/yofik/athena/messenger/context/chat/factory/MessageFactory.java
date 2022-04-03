package ru.yofik.athena.messenger.context.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.context.chat.dto.ChatJpaDto;
import ru.yofik.athena.messenger.context.chat.dto.MessageJpaDto;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.chat.model.Message;
import ru.yofik.athena.messenger.context.user.model.User;

import java.time.Instant;
import java.time.ZoneId;

@Component
public class MessageFactory {
    public Message create(String text, User sender, Chat chat) {
        return new Message(
                0,
                text,
                sender.getId(),
                chat.getId(),
                Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime()
        );
    }

    public Message from(MessageJpaDto messageJpaDto) {
        return new Message(
                messageJpaDto.getId(),
                messageJpaDto.getText(),
                messageJpaDto.getSenderId(),
                messageJpaDto.getChat().getId(),
                messageJpaDto.getDate()
        );
    }

    public MessageJpaDto to(Message message, ChatJpaDto chatJpaDto) {
        return new MessageJpaDto(
                message.getId(),
                message.getText(),
                message.getSenderId(),
                chatJpaDto,
                message.getDate().atZone(ZoneId.of("UTC")).toLocalDateTime()
        );
    }
}
