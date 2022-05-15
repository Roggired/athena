package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.chat.model.Topic;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.ChatEntity;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.MessageEntity;

import java.time.ZoneId;
import java.util.ArrayList;

@Component
public class MessageFactory {
    private final TopicFactory topicFactory;

    public MessageFactory(TopicFactory topicFactory) {
        this.topicFactory = topicFactory;
    }

    public Message fromEntity(MessageEntity messageEntity) {
        var topic = messageEntity.getTopic() == null ? Topic.DEFAULT_TOPIC : topicFactory.fromEntity(messageEntity.getTopic());
        return new Message(
                messageEntity.getId(),
                messageEntity.getText(),
                messageEntity.getSenderId(),
                messageEntity.getChat().getId(),
                messageEntity.getCreationDate(),
                messageEntity.getModificationDate(),
                messageEntity.getOwningUserIds(),
                messageEntity.getViewedByUserIds(),
                topic,
                messageEntity.isPinned()
        );
    }

    public MessageEntity toEntity(Message message, ChatEntity chatEntity) {
        var topicEntity = message.getTopic().equals(Topic.DEFAULT_TOPIC) ? null : topicFactory.toEntity(message.getTopic());
        return new MessageEntity(
                message.getId(),
                message.getText(),
                message.getSenderId(),
                chatEntity,
                message.getCreationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                message.getModificationDate().atZone(ZoneId.of("UTC")).toLocalDateTime(),
                new ArrayList<>(message.getOwningUserIds()),
                new ArrayList<>(message.getViewedByUserIds()),
                topicEntity,
                message.isPinned()
        );
    }
}
