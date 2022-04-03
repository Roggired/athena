package ru.yofik.athena.messenger.context.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.chat.api.request.CreateChatRequest;
import ru.yofik.athena.messenger.context.chat.model.Chat;
import ru.yofik.athena.messenger.context.chat.view.ChatFullView;
import ru.yofik.athena.messenger.context.chat.view.ChatView;

import java.util.List;

@Service
public interface ChatService {
    ChatView create(CreateChatRequest request);
    void delete(long id);
    Chat get(long id);
    ChatFullView getFull(long id);
    List<ChatView> getAll();
}
