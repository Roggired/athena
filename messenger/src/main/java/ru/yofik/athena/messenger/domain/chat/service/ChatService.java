package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.chat.request.CreateGroupChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.CreatePersonalChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.InviteUserToGroupChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;

import java.util.List;

@Service
public interface ChatService {
    Chat createPersonalChat(CreatePersonalChatRequest request);
    void deleteById(long id);
    Chat getById(long id);
    Page<Chat> getPageForCurrentUser(Page.Meta pageMeta);
    void checkThatChatExists(long id);

    Chat createGroupChat(CreateGroupChatRequest request);
    void inviteUserToGroupChat(InviteUserToGroupChatRequest request);
    Chat acceptInvitation(String id);
    void leaveChat(long chatId);
    List<JoinChatInvitation> getAllJoinChatInvitations();
}
