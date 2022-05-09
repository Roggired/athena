package ru.yofik.athena.messenger.api.http.chat.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.view.MessageView;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class MessageViewMapper implements ConversionServiceConfig.Mapper<Message, MessageView> {
    @Override
    public MessageView convert(Message message) {
        return new MessageView(
                message.getId(),
                message.getText(),
                message.getSenderId(),
                message.getChatId(),
                message.getCreationDate().toString(),
                message.getModificationDate().toString()
        );
    }
}
