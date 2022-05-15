package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import java.util.UUID;

@Component
public class JoinChatInvitationGenerator {
    private final UserService userService;

    public JoinChatInvitationGenerator(UserService userService) {
        this.userService = userService;
    }

    public JoinChatInvitation generateInvitation(long recipientId, Chat chat) {
        var sender = userService.getCurrentUser();
        var code = UUID.randomUUID().toString();

        return new JoinChatInvitation(
                code,
                recipientId,
                sender,
                chat
        );
    }
}
