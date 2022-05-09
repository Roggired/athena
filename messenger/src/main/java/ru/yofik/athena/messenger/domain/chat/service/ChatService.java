package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.api.http.chat.request.CreateChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;

import java.util.List;

@Service
public interface ChatService {
    Chat create(CreateChatRequest request);
    void delete(long id);
    Chat getWithoutMessages(long id);
    Chat getFull(long id);
    List<Chat> getAllForCurrentUser();
}
