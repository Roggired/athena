package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.messenger.domain.chat.model.Message;

import java.util.List;

public interface MessageRepository {
    Message save(Message message);
    List<Message> saveAll(List<Message> messages);
    void deleteById(long id);
    void deleteAllById(List<Long> ids);
    Message getById(long id);
    List<Message> getAll();
    List<Message> getAllById(List<Long> ids);
}
