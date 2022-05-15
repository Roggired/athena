package ru.yofik.athena.messenger.domain.chat.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.api.http.chat.request.CreateTopicRequest;
import ru.yofik.athena.messenger.api.http.chat.request.UpdateTopicRequest;
import ru.yofik.athena.messenger.domain.chat.model.Topic;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.domain.chat.repository.TopicRepository;
import ru.yofik.athena.messenger.domain.notification.model.DeletedTopicNotification;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TopicServiceImpl implements TopicService {
    private final TopicRepository topicRepository;
    private final MessageRepository messageRepository;
    private final ChatService chatService;
    private final NotificationService notificationService;


    public TopicServiceImpl(
            TopicRepository topicRepository,
            MessageRepository messageRepository,
            ChatService chatService,
            NotificationService notificationService
    ) {
        this.topicRepository = topicRepository;
        this.messageRepository = messageRepository;
        this.chatService = chatService;
        this.notificationService = notificationService;
    }

    @Override
    public Topic createTopic(long chatId, CreateTopicRequest request) {
        var topic = Topic.newTopic(request.name, chatId);
        return topicRepository.save(topic);
    }

    @Override
    public void updateTopic(long id, UpdateTopicRequest request) {
        var topic = topicRepository.getById(id);
        topic.setName(request.name);
        topicRepository.save(topic);
    }

    @Override
    public void deleteAllTopicsById(long chatId, List<Long> ids) {
        var chat = chatService.getById(chatId);
        var chatTopics = topicRepository.getAllByChatId(chat.getId());
        ids.forEach(topicId -> {
            if (chatTopics.stream()
                    .filter(topic -> topic.getId() == topicId)
                    .findFirst()
                    .isEmpty()) {
                throw new ResourceNotFoundException();
            }
        });

        if (ids.contains(chat.getLastMessage().getTopic().getId())) {
            chat.setLastMessage(
                    messageRepository.save(
                            chat.getLastMessage()
                                    .setTopic(Topic.DEFAULT_TOPIC)
                    )
            );
        }

        topicRepository.deleteAllById(ids);

        notificationService.sendNotification(
                new DeletedTopicNotification(
                        chat,
                        ids
                )
        );
    }

    @Override
    public Topic getById(long id) {
        return topicRepository.getById(id);
    }

    @Override
    public List<Topic> getAllByChatId(long chatId) {
        chatService.checkThatChatExists(chatId);
        return topicRepository.getAllByChatId(chatId);
    }
}
