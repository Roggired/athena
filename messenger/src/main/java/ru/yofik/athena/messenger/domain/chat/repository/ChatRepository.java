package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.messenger.domain.chat.model.Chat;

import java.util.List;
import java.util.Optional;

public interface ChatRepository {
    Chat getById(long id);
    Chat save(Chat chat);
    void delete(long id);
    List<Chat> getAll();
    Page<Chat> getPage(Page.Meta pageMeta);
    Page<Chat> getPageByUserId(Page.Meta pageMeta, long userId);
    boolean existsById(long id);
    Optional<Chat> getByUserIds(long userAId, long userBId);
}
