package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.ChatEntity;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.MessageEntity;

import java.time.ZoneId;

@Component
public class MessageFactory {
    public Message fromEntity(MessageEntity messageEntity) {
        return new Message(
                messageEntity.getId(),
                messageEntity.getText(),
                messageEntity.getSenderId(),
                messageEntity.getChat().getId(),
                messageEntity.getCreationDate(),
                messageEntity.getModificationDate(),
                messageEntity.getOwningUserIds(),
                messageEntity.getViewedByUserIds()
        );
    }

    public MessageEntity toEntity(Message message, ChatEntity chatEntity) {
        return new MessageEntity(
                message.getId(),
                message.getText(),
                message.getSenderId(),
                chatEntity,
                message.getCreationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getModificationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getOwningUserIds(),
                message.getViewedByUserIds()
        );
    }
}
