package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.domain.chat.model.Chat;

import java.util.List;

public interface ChatRepository {
    Chat getById(long id);
    Chat save(Chat chat);
    void delete(long id);
    List<Chat> getAll();
    Page<Chat> getPage(Page.Meta pageMeta);
}
