package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.*;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.api.http.chat.view.MessageView;
import ru.yofik.athena.messenger.api.http.chat.view.TopicView;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;
import ru.yofik.athena.messenger.domain.chat.service.MessageService;
import ru.yofik.athena.messenger.domain.chat.service.TopicService;

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
    @Autowired
    private TopicService topicService;

    @GetMapping
    public MessengerV1Response getPageOfChatsForCurrentUser(
            @Valid Page.Meta pageMeta
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                chatService.getPageForCurrentUser(pageMeta)
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
                        chatService.getById(id),
                        ChatView.class
                )
        );
    }

    @GetMapping("/{id}/messages")
    public MessengerV1Response getPageOfChatMessages(
            @PathVariable("id") long chatId,
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

    @PostMapping("/{chatId}/messages/viewed")
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

    @GetMapping("/{chatId}/topics")
    public MessengerV1Response getTopicsByChatId(
            @PathVariable("chatId") long chatId
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                topicService.getAllByChatId(chatId)
                        .stream()
                        .map(topic -> conversionService.convert(topic, TopicView.class))
                        .collect(Collectors.toList())
        );
    }

    @GetMapping("/{chatId}/topics/{topicId}")
    public MessengerV1Response getTopic(
        @PathVariable("topicId") long topicId
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_RETURNED,
                conversionService.convert(
                        topicService.getById(topicId),
                        TopicView.class
                )
        );
    }

    @PostMapping("/{chatId}/topics")
    public MessengerV1Response createTopic(
            @PathVariable("chatId") long chatId,
            @RequestBody @Valid CreateTopicRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                conversionService.convert(
                        topicService.createTopic(chatId, request),
                        TopicView.class
                )
        );
    }

    @PutMapping("/{chatId}/topics/{topicId}")
    public MessengerV1Response updateTopic(
            @PathVariable("topicId") long topicId,
            @RequestBody @Valid UpdateTopicRequest request
    ) {
        topicService.updateTopic(topicId, request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_UPDATED,
                "Topic has been updated"
        );
    }

    @DeleteMapping("/{chatId}/topics/{topicId}")
    public MessengerV1Response deleteTopic(
            @PathVariable("chatId") long chatId,
            @PathVariable("topicId") long topicId
    ) {
        topicService.deleteAllTopicsById(chatId, List.of(topicId));
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Topic has been deleted"
        );
    }

    @DeleteMapping("/{chatId}/topics")
    public MessengerV1Response deleteTopics(
            @PathVariable("chatId") long chatId,
            @RequestBody @Valid DeleteTopicRequest request
    ) {
        topicService.deleteAllTopicsById(chatId, request.deletedTopicIds);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Existed topics has been deleted"
        );
    }

    @PostMapping("/{chatId}/messages/pinned")
    public MessengerV1Response pinMessage(
            @RequestBody @Valid PinMessageRequest request
    ) {
        messageService.pinMessage(request);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                "Message has been pinned"
        );
    }

    @DeleteMapping("/{chatId}/messages/pinned/{messageId}")
    public MessengerV1Response unpinMessage(
            @PathVariable("messageId") long messageId
    ) {
        messageService.unpinMessage(messageId);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Message has been unpinned"
        );
    }

    @PutMapping("/{chatId}/messages/{messageId}/topic")
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

    @GetMapping("/{chatId}/messagesWithTopic/{topicId}")
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
