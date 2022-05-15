package ru.yofik.athena.messenger.infrastructure.storage.sql.chat;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.domain.chat.model.Topic;
import ru.yofik.athena.messenger.domain.chat.repository.TopicRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory.TopicFactory;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository.CrudTopicRepository;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TopicRepositoryImpl implements TopicRepository {
    private final CrudTopicRepository crudTopicRepository;
    private final TopicFactory topicFactory;

    public TopicRepositoryImpl(CrudTopicRepository crudTopicRepository, TopicFactory topicFactory) {
        this.crudTopicRepository = crudTopicRepository;
        this.topicFactory = topicFactory;
    }

    @Override
    public Topic getById(long id) {
        return crudTopicRepository.findById(id)
                .map(topicFactory::fromEntity)
                .orElseThrow(ResourceNotFoundException::new);
    }

    @Override
    public List<Topic> getAllByChatId(long chatId) {
        return crudTopicRepository.findAllByChatId(chatId)
                .stream()
                .map(topicFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Topic save(Topic topic) {
        return topicFactory.fromEntity(
                crudTopicRepository.save(
                        topicFactory.toEntity(topic)
                )
        );
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        crudTopicRepository.deleteAllById(ids);
    }
}
