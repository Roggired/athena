package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.CreateGroupChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.InviteUserToGroupChatRequest;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.api.http.chat.view.JoinChatInvitationView;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/chats/group")
public class GroupChatController {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ChatService chatService;


    @PostMapping
    public MessengerV1Response createGroupChat(
            @RequestBody @Valid CreateGroupChatRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                chatService.createGroupChat(request)
        );
    }

    @DeleteMapping("/{chatId}")
    public MessengerV1Response leaveChat(
            @PathVariable("chatId") long chatId
    ) {
        chatService.leaveChat(chatId);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "You have leaved this chat"
        );
    }

    @PostMapping("/invitations")
    public MessengerV1Response inviteUser(
            @RequestBody @Valid InviteUserToGroupChatRequest request
    ) {
        chatService.inviteUserToGroupChat(request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                "JoinChatInvitation has been created"
        );
    }

    @DeleteMapping("/invitations/{invitationId}")
    public MessengerV1Response acceptInvitation(
            @PathVariable("invitationId") String invitationId
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                conversionService.convert(chatService.acceptInvitation(invitationId), ChatView.class)
        );
    }

    @GetMapping("/invitations")
    public MessengerV1Response getAllMineInvitations() {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getAllJoinChatInvitations()
                        .stream()
                        .map(invitation -> conversionService.convert(invitation, JoinChatInvitationView.class))
        );
    }
}
