package ru.yofik.athena.messenger.api.http.chat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.web.bind.annotation.*;
import ru.yofik.athena.messenger.api.http.MessengerV1Response;
import ru.yofik.athena.messenger.api.http.MessengerV1ResponseStatus;
import ru.yofik.athena.messenger.api.http.chat.request.CreateTopicRequest;
import ru.yofik.athena.messenger.api.http.chat.request.DeleteTopicRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateTopicRequest;
import ru.yofik.athena.messenger.api.http.chat.view.TopicView;
import ru.yofik.athena.messenger.domain.chat.service.TopicService;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/chats/{chatId}/topics")
public class TopicController {
    @Autowired
    private ConversionService conversionService;
    @Autowired
    private TopicService topicService;


    @GetMapping
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

    @GetMapping("/{topicId}")
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

    @PostMapping
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

    @PutMapping("/{topicId}")
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

    @DeleteMapping("/{topicId}")
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

    @DeleteMapping
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
}
