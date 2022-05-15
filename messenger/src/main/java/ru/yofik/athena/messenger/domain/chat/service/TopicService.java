package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.api.http.chat.request.CreateTopicRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateTopicRequest;
import ru.yofik.athena.messenger.domain.chat.model.Topic;

import java.util.List;

@Service
public interface TopicService {
    Topic createTopic(long chatId, CreateTopicRequest request);
    void updateTopic(long id, UpdateTopicRequest request);
    void deleteAllTopicsById(long chatId, List<Long> ids);
    Topic getById(long id);
    List<Topic> getAllByChatId(long chatId);
}
