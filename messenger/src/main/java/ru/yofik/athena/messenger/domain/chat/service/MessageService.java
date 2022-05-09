package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.api.http.chat.request.SendMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateMessageRequest;

@Service
public interface MessageService {
    void sendMessage(SendMessageRequest request);
    void deleteMessages(long chatId, DeleteMessagesRequest request, boolean isGlobal);
    void updateMessage(long chatId, long messageId, UpdateMessageRequest request);
}
