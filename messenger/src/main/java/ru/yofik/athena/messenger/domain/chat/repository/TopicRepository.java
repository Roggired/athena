package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.messenger.domain.chat.model.Topic;

import java.util.List;

public interface TopicRepository {
    Topic getById(long id);
    List<Topic> getAllByChatId(long chatId);
    Topic save(Topic topic);
    void deleteAllById(List<Long> ids);
}
