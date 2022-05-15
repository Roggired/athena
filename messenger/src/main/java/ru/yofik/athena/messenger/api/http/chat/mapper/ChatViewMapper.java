package ru.yofik.athena.messenger.api.http.chat.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.api.http.user.mapper.UserViewMapper;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

import java.util.stream.Collectors;

@Component
public class ChatViewMapper implements ConversionServiceConfig.Mapper<Chat, ChatView> {
    @Override
    public ChatView convert(Chat chat) {
        var userViewMapper = new UserViewMapper();
        var messageViewMapper = new MessageViewMapper();
        return new ChatView(
                chat.getId(),
                chat.getName(),
                chat.getType().name(),
                chat.getUsers().stream()
                        .map(userViewMapper::convert)
                        .collect(Collectors.toList()),
                chat.getLastMessage() == null ? null : messageViewMapper.convert(chat.getLastMessage())
        );
    }
}
