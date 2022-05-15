package ru.yofik.athena.messenger.domain.chat.repository;

import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;

import java.util.List;

public interface JoinChatInvitationRepository {
    JoinChatInvitation save(JoinChatInvitation joinChatInvitation);
    List<JoinChatInvitation> getAllByRecipientId(long recipientId);
    JoinChatInvitation getById(String id);
    void deleteById(String id);
}
