package ru.yofik.athena.messenger.context.chat.api.resource.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.context.chat.api.request.CreateChatRequest;
import ru.yofik.athena.messenger.context.chat.api.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.context.chat.api.request.SendMessageRequest;
import ru.yofik.athena.messenger.context.chat.api.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.context.chat.service.ChatService;
import ru.yofik.athena.messenger.context.chat.service.MessageService;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1Response;
import ru.yofik.athena.messenger.infrastructure.response.MessengerV1ResponseStatus;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatResource {
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    @GetMapping
    public MessengerV1Response getAllChatsForCurrentUser() {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getAllForCurrentUser()
        );
    }

    @GetMapping("/{id}")
    public MessengerV1Response getChat(
            @PathVariable("id") long id
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getWithoutMessages(id).toView()
        );
    }

    @GetMapping("/{id}/fullView")
    public MessengerV1Response getFullChat(
            @PathVariable("id") long id
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getFull(id)
        );
    }

    @PostMapping
    public MessengerV1Response createChat(
            @RequestBody @Valid CreateChatRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                chatService.create(request)
        );
    }

    @DeleteMapping("/{id}")
    public MessengerV1Response deleteChat(
            @PathVariable("id") long id
    ) {
        chatService.delete(id);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Chat has been deleted"
        );
    }

    @PostMapping("/{id}/messages")
    public MessengerV1Response sendMessage(
            @PathVariable("id") long chatId,
            @RequestBody @Valid SendMessageRequest request
    ) {
        request.chatId = chatId;
        messageService.sendMessage(request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                "Message has been send"
        );
    }

    @PutMapping("/{chatId}/messages/{messageId}")
    public MessengerV1Response updateMessage(
            @PathVariable("chatId") long chatId,
            @PathVariable("messageId") long messageId,
            @RequestBody @Valid UpdateMessageRequest request
    ) {
        messageService.updateMessage(chatId, messageId, request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                "Message has been updated"
        );
    }

    @DeleteMapping("/{id}/messages")
    public MessengerV1Response deleteMessages(
            @PathVariable("id") long chatId,
            @RequestBody @Valid DeleteMessagesRequest request
    ) {
        messageService.deleteMessages(chatId, request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Messages has been deleted"
        );
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public MessengerV1Response deleteMessage(
            @PathVariable("chatId") long chatId,
            @PathVariable("messageId") long messageId
    ) {
        messageService.deleteMessage(chatId, messageId);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Message has been deleted"
        );
    }
}
