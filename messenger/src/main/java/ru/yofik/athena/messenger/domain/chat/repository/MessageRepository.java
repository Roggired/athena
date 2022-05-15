package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.domain.chat.model.Message;

import java.util.List;

public interface MessageRepository {
    Message save(Message message);
    List<Message> saveAll(List<Message> messages);
    void deleteById(long id);
    void deleteAllById(List<Long> ids);
    void deleteAllByChatId(long chatId);
    void deleteAllByChatIdAndSenderId(long chatId, long senderId);
    Message getById(long id);
    List<Message> getAllById(List<Long> ids);
    Page<Message> getPageByChatIdAndOwningUserId(
            Page.Meta pageMeta,
            long chatId,
            long userId
    );
    Page<Message> getPageByChatIdAndOwningUserIdAndTopicId(
            Page.Meta pageMeta,
            long chatId,
            long userId,
            long topicId
    );
}
