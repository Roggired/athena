package ru.yofik.athena.messenger.api.http.chat.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.view.ChatFullView;
import ru.yofik.athena.messenger.api.http.user.mapper.UserViewMapper;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

import java.util.stream.Collectors;

@Component
public class ChatFullViewMapper implements ConversionServiceConfig.Mapper<Chat, ChatFullView> {
    @Override
    public ChatFullView convert(Chat chat) {
        var userViewMapper = new UserViewMapper();
        var messageViewMapper = new MessageViewMapper();
        return new ChatFullView(
                chat.getId(),
                chat.getName(),
                chat.getUsers().stream()
                        .map(userViewMapper::convert)
                        .collect(Collectors.toList()),
                chat.getMessages().stream()
                        .map(messageViewMapper::convert)
                        .collect(Collectors.toList())
        );
    }
}
