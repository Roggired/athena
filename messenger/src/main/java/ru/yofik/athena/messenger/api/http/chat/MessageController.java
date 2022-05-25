package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.*;
import ru.yofik.athena.messenger.api.http.chat.view.MessageView;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;
import ru.yofik.athena.messenger.domain.chat.service.MessageService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/chats/{chatId}/messages")
public class MessageController {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ChatService chatService;
    @Autowired
    private MessageService messageService;


    @GetMapping
    public MessengerV1Response getPageOfChatMessages(
            @PathVariable("chatId") long chatId,
            @Valid Page.Meta pageMeta
    ) {
        var chat = chatService.getById(chatId);
        var page = messageService.getPageFor(chat, pageMeta);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                page.map(message -> conversionService.convert(
                        message,
                        MessageView.class
                ))
        );
    }

    @PostMapping
    public MessengerV1Response sendMessage(
            @PathVariable("chatId") long chatId,
            @RequestBody @Valid SendMessageRequest request
    ) {
        request.chatId = chatId;
        messageService.sendMessage(request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                "Message has been send"
        );
    }

    @PutMapping("/{messageId}")
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

    @DeleteMapping
    public MessengerV1Response deleteMessages(
            @PathVariable("chatId") long chatId,
            @RequestBody @Valid DeleteMessagesRequest request,
            @RequestParam("global") boolean isGlobal
    ) {
        messageService.deleteMessages(chatId, request, isGlobal);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Messages has been deleted"
        );
    }

    @DeleteMapping("/{messageId}")
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

    @PostMapping("/viewed")
    public MessengerV1Response viewMessages(
            @PathVariable("chatId") long chatId,
            @RequestBody ViewMessagesRequest request
    ) {
        messageService.viewMessage(request.messageIds);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                "Messages has been viewed"
        );
    }

    @PostMapping("/pinned")
    public MessengerV1Response pinMessage(
            @RequestBody @Valid PinMessageRequest request
    ) {
        messageService.pinMessage(request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                "Message has been pinned"
        );
    }

    @DeleteMapping("pinned/{messageId}")
    public MessengerV1Response unpinMessage(
            @PathVariable("messageId") long messageId
    ) {
        messageService.unpinMessage(messageId);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Message has been unpinned"
        );
    }

    @PutMapping("/{messageId}/topic")
    public MessengerV1Response changedMessageTopic(
            @PathVariable("messageId") long messageId,
            @RequestBody @Valid ChangeTopicRequest request
    ) {
        messageService.changeTopic(messageId, request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                "Message has changed its topic"
        );
    }

    @GetMapping("/withTopic/{topicId}")
    public MessengerV1Response getMessagePageByTopic(
            @PathVariable("chatId") long chatId,
            @PathVariable("topicId") long topicId,
            @Valid Page.Meta pageMeta
    ) {
        var chat = chatService.getById(chatId);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                messageService.getPageByTopicFor(
                        chat,
                        pageMeta,
                        topicId
                ).map(message -> conversionService.convert(message, MessageView.class))
        );
    }
}
