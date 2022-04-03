package ru.yofik.athena.messenger.context.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.context.chat.api.request.SendMessageRequest;

@Service
public interface MessageService {
    void sendMessage(SendMessageRequest request);
}
