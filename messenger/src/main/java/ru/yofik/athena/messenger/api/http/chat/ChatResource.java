package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.CreateChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteMessagesRequest;
import ru.yofik.athena.messenger.api.http.chat.request.SendMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateMessageRequest;
import ru.yofik.athena.messenger.api.http.chat.view.ChatFullView;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;
import ru.yofik.athena.messenger.domain.chat.service.MessageService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatResource {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;

    @GetMapping
    public MessengerV1Response getAllChatsForCurrentUser() {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getAllForCurrentUser()
                        .stream()
                        .map(chat -> conversionService.convert(chat, ChatView.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public MessengerV1Response getChat(
            @PathVariable("id") long id
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        chatService.getWithoutMessages(id),
                        ChatView.class
                )
        );
    }

    @GetMapping("/{id}/fullView")
    public MessengerV1Response getFullChat(
            @PathVariable("id") long id
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        chatService.getFull(id),
                        ChatFullView.class
                )
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
            @RequestBody @Valid DeleteMessagesRequest request,
            @RequestParam("global") boolean isGlobal
    ) {
        messageService.deleteMessages(chatId, request, isGlobal);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Messages has been deleted"
        );
    }

    @DeleteMapping("/{chatId}/messages/{messageId}")
    public MessengerV1Response deleteMessage(
            @PathVariable("chatId") long chatId,
            @PathVariable("messageId") long messageId,
            @RequestParam("global") boolean isGlobal
    ) {
        var deleteMessagesRequest = new DeleteMessagesRequest();
        deleteMessagesRequest.ids = List.of(messageId);
        messageService.deleteMessages(
                chatId,
                deleteMessagesRequest,
                isGlobal
        );
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Message has been deleted"
        );
    }
}
