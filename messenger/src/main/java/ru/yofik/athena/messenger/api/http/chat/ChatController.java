package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.CreateGroupChatRequest;
import ru.yofik.athena.messenger.api.http.chat.request.CreatePersonalChatRequest;
import ru.yofik.athena.messenger.api.http.chat.view.ChatView;
import ru.yofik.athena.messenger.domain.chat.service.ChatService;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats")
public class ChatController {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private ChatService chatService;

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

    @PostMapping
    public MessengerV1Response createPersonalChat(
            @RequestBody @Valid CreatePersonalChatRequest request
    ) {
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_CREATED,
                conversionService.convert(
                        chatService.createPersonalChat(request),
                        ChatView.class
                )
        );
    }

    @DeleteMapping("/{id}")
    public MessengerV1Response deleteChat(
            @PathVariable("id") long id
    ) {
        chatService.deleteById(id);
        return MessengerV1Response.of(
                MessengerV1ResponseStatus.RESOURCE_DELETED,
                "Chat has been deleted"
        );
    }
}
