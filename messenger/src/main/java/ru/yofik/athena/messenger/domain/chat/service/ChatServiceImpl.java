package ru.yofik.athena.messenger.domain.chat.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.annotation.RequestScope;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.exception.InvalidDataException;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.api.http.chat.request.CreateGroupChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.CreatePersonalChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.InviteUserToGroupChatRequest;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.model.ChatType;
import ru.yofik.athena.messenger.domain.chat.model.JoinChatInvitation;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.chat.repository.ChatRepository;
import ru.yofik.athena.messenger.domain.chat.repository.JoinChatInvitationRepository;
import ru.yofik.athena.messenger.domain.notification.model.LeavedUserNotification;
import ru.yofik.athena.messenger.domain.notification.model.NewInvitationNotification;
import ru.yofik.athena.messenger.domain.notification.model.NewUserNotification;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.domain.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequestScope
@Log4j2
public class ChatServiceImpl implements ChatService {
    private final UserService userService;
    private final ChatRepository chatRepository;
    private final MessageService messageService;
    private final NotificationService notificationService;
    private final JoinChatInvitationRepository joinChatInvitationRepository;
    private final JoinChatInvitationGenerator joinChatInvitationGenerator;

    public ChatServiceImpl(
            UserService userService,
            ChatRepository chatRepository,
            MessageService messageService,
            NotificationService notificationService,
            JoinChatInvitationRepository joinChatInvitationRepository,
            JoinChatInvitationGenerator joinChatInvitationGenerator) {
        this.userService = userService;
        this.chatRepository = chatRepository;
        this.messageService = messageService;
        this.notificationService = notificationService;
        this.joinChatInvitationRepository = joinChatInvitationRepository;
        this.joinChatInvitationGenerator = joinChatInvitationGenerator;
    }

    @Override
    public Chat createPersonalChat(CreatePersonalChatRequest request) {
        var user = userService.getCurrentUser();
        var targetUser = userService.getById(request.targetUserId);

        var chat = Chat.newPersonalChat(user, targetUser);
        chat.markOnlineUsers(notificationService);

        return chatRepository.save(chat);
    }

    @Override
    public void deleteById(long id) {
        var chat = chatRepository.getById(id);

        if (chat.getType() == ChatType.GROUP) {
            leaveChat(id);
        } else {
            messageService.deleteMessagesByChatId(chat.getId(), true);
            chatRepository.delete(chat.getId());
        }
    }

    @Override
    public Chat getById(long id) {
        var chat = chatRepository.getById(id)
                .chooseChatNameFor(userService.getCurrentUser())
                .markOnlineUsers(notificationService);
        chat.setLastMessage(messageService.getLastFor(chat));
        return chat;
    }

    @Override
    public Page<Chat> getPageForCurrentUser(Page.Meta pageMeta) {
        var page = chatRepository.getPage(pageMeta);
        return page.map(chat -> {
            chat.chooseChatNameFor(userService.getCurrentUser());
            chat.markOnlineUsers(notificationService);
            chat.setLastMessage(messageService.getLastFor(chat));
            return chat;
        });
    }

    @Override
    public void checkThatChatExists(long id) {
        if (!chatRepository.existsById(id)) {
            throw new ResourceNotFoundException();
        }
    }

    @Override
    public Chat createGroupChat(CreateGroupChatRequest request) {
        var user = userService.getCurrentUser();
        var chat = Chat.newGroupChat(request.name, user);
        chat.markOnlineUsers(notificationService);
        return chatRepository.save(chat);
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public Chat acceptInvitation(String id) {
        var joinChatInvitation = joinChatInvitationRepository.getById(id);
        var chat = chatRepository.getById(joinChatInvitation.getChat().getId());
        var recipient = userService.getCurrentUser();

        if (joinChatInvitation.getRecipientId() != recipient.getId()) {
            log.warn("User with login: " + recipient.getLogin() + " is trying to accept the invitation to group chat id: "
                    + chat.getId() + ", but this invitation is not his. I will not delete the invitation due to minor severity of the problem");
            throw new InvalidDataException("This is not yours invitation");
        }

        if (chat.getUsers().contains(recipient)) {
            log.warn("User with login: " + recipient.getLogin() + " is trying to accept the invitation to group chat id: "
                    + chat.getId() + ", but he is already in chat. Therefore, delete invitation with id: " + id);
            joinChatInvitationRepository.deleteById(id);
            throw new InvalidDataException("You are already in chat");
        }

        chat.addUser(recipient);
        joinChatInvitationRepository.deleteById(id);
        chat = chatRepository.save(chat);

        notificationService.sendNotification(
                new NewUserNotification(
                        chat,
                        recipient,
                        chat.getId()
                )
        );
        return chat;
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void inviteUserToGroupChat(InviteUserToGroupChatRequest request) {
        var recipient = userService.getById(request.userId);
        var chat = chatRepository.getById(request.chatId);

        if (chat.getUsers().contains(recipient)) {
            throw new InvalidDataException("User already in chat");
        }

        var joinChatInvitation = joinChatInvitationGenerator.generateInvitation(
                recipient.getId(),
                chat
        );
        joinChatInvitationRepository.save(joinChatInvitation);

        notificationService.sendNotification(
                new NewInvitationNotification(
                        List.of(recipient.getId()),
                        joinChatInvitation.getId(),
                        userService.getCurrentUser(),
                        chat
                )
        );
    }

    @Override
    @Transactional(
            isolation = Isolation.REPEATABLE_READ
    )
    public void leaveChat(long chatId) {
        var user = userService.getCurrentUser();
        var chat = chatRepository.getById(chatId);

        if (!chat.getUsers().contains(user)) {
            log.warn("User with login: " + user.getLogin() + " is trying to leave the chat with id: " + chat.getId() +
                    ", but he doesn't belong to this chat");
            throw new InvalidDataException("You are not in this chat");
        }

        messageService.deleteMessagesByChatId(chat.getId(), false);
        chat.removeUser(user);

        if (chat.getUsers().isEmpty()) {
            chatRepository.save(chat);
            chatRepository.delete(chat.getId());
        } else {
            chatRepository.save(chat);

            notificationService.sendNotification(
                    new LeavedUserNotification(
                            chat,
                            user,
                            chat.getId()
                    )
            );
        }
    }

    @Override
    public List<JoinChatInvitation> getAllJoinChatInvitations() {
        var recipient = userService.getCurrentUser();
        return joinChatInvitationRepository.getAllByRecipientId(recipient.getId());
    }
}
