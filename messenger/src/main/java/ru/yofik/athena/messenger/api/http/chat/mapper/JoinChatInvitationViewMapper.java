package ru.yofik.athena.messenger.api.http.chat.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.view.JoinChatInvitationView;
import ru.yofik.athena.messenger.api.http.user.mapper.UserViewMapper;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class JoinChatInvitationViewMapper implements ConversionServiceConfig.Mapper<JoinChatInvitation, JoinChatInvitationView> {
    @Override
    public JoinChatInvitationView convert(JoinChatInvitation invitation) {
        var userViewMapper = new UserViewMapper();
        var chatViewMapper = new ChatViewMapper();

        return new JoinChatInvitationView(
                invitation.getId(),
                userViewMapper.convert(invitation.getSender()),
                chatViewMapper.convert(invitation.getChat())
        );
    }
}
