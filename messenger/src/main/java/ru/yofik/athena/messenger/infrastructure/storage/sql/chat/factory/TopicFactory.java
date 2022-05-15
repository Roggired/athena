package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.Topic;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.TopicEntity;

@Component
public class TopicFactory {
    public Topic fromEntity(TopicEntity topicEntity) {
        return new Topic(
                topicEntity.getId(),
                topicEntity.getName(),
                topicEntity.getChatId()
        );
    }

    public TopicEntity toEntity(Topic topic) {
        return new TopicEntity(
                topic.getId(),
                topic.getName(),
                topic.getChatId()
        );
    }
}
