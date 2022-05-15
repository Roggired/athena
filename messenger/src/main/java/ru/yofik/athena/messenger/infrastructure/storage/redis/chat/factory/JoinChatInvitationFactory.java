package ru.yofik.athena.messenger.infrastructure.storage.redis.chat.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;
import ru.yofik.athena.messenger.domain.user.service.UserService;
import ru.yofik.athena.messenger.infrastructure.storage.redis.chat.entity.JoinChatInvitationEntity;

@Component
public class JoinChatInvitationFactory {
    private final UserService userService;
    private final ChatService chatService;


    public JoinChatInvitationFactory(UserService userService, ChatService chatService) {
        this.userService = userService;
        this.chatService = chatService;
    }

    public JoinChatInvitation fromEntity(JoinChatInvitationEntity joinChatInvitationEntity) {
        return new JoinChatInvitation(
                joinChatInvitationEntity.getId(),
                joinChatInvitationEntity.getRecipientId(),
                userService.getById(joinChatInvitationEntity.getSenderId()),
                chatService.getById(joinChatInvitationEntity.getChatId())
        );
    }

    public JoinChatInvitationEntity toEntity(JoinChatInvitation joinChatInvitation) {
        return new JoinChatInvitationEntity(
                joinChatInvitation.getId(),
                joinChatInvitation.getRecipientId(),
                joinChatInvitation.getSender().getId(),
                joinChatInvitation.getChat().getId()
        );
    }
}
