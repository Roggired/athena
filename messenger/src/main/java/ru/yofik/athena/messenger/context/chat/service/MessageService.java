package ru.yofik.athena.messenger.context.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.chat.api.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.context.chat.api.request.SendMessageRequest;
import ru.yofik.athena.messenger.context.chat.api.request.UpdateMessageRequest;

import java.util.List;

@Service
public interface MessageService {
    void sendMessage(SendMessageRequest request);
    void deleteMessage(long chatId, long messageId, boolean isGlobal);
    void deleteMessages(long chatId, DeleteMessagesRequest request, boolean isGlobal);
    void updateMessage(long chatId, long messageId, UpdateMessageRequest request);
}
