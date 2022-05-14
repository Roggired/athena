package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.api.http.chat.request.SendMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.Message;

import java.util.List;

@Service
public interface MessageService {
    void sendMessage(SendMessageRequest request);
    void deleteMessages(long chatId, DeleteMessagesRequest request, boolean isGlobal);
    void updateMessage(long chatId, long messageId, UpdateMessageRequest request);
    Page<Message> getPageFor(Chat chat, Page.Meta pageMeta);
    Message getLastFor(Chat chat);
    void deleteMessagesByChatId(long chatId);
    void viewMessage(List<Long> messageIds);
}
