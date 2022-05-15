package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.chat.request.CreateChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;

@Service
public interface ChatService {
    Chat create(CreateChatRequest request);
    void delete(long id);
    Chat getById(long id);
    Page<Chat> getPageForCurrentUser(Page.Meta pageMeta);
    void checkThatChatExists(long id);
}
