package ru.yofik.athena.messenger.infrastructure.storage.sql.chat;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.domain.chat.model.Message;
import ru.yofik.athena.messenger.domain.chat.repository.MessageRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory.MessageFactory;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository.CrudChatRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository.CrudMessageRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageRepositoryImpl implements MessageRepository {
    private final CrudMessageRepository crudMessageRepository;
    private final MessageFactory messageFactory;
    private final CrudChatRepository crudChatRepository;

    public MessageRepositoryImpl(
            CrudMessageRepository crudMessageRepository,
            MessageFactory messageFactory,
            CrudChatRepository crudChatRepository
    ) {
        this.crudMessageRepository = crudMessageRepository;
        this.messageFactory = messageFactory;
        this.crudChatRepository = crudChatRepository;
    }

    @Override
    public Message save(Message message) {
        var chatEntity = crudChatRepository.findById(message.getChatId())
                .orElseThrow(IllegalStateException::new);
        return messageFactory.fromEntity(
                crudMessageRepository.save(
                    messageFactory.toEntity(
                            message,
                            chatEntity
                    )
                )
        );
    }

    @Override
    public List<Message> saveAll(List<Message> messages) {
        if (messages.isEmpty()) {
            return new ArrayList<>();
        }

        var chatEntity = crudChatRepository.findById(messages.get(0).getChatId())
                .orElseThrow(IllegalStateException::new);
        return crudMessageRepository.saveAll(
                    messages.stream()
                            .map(message -> messageFactory.toEntity(message, chatEntity))
                            .collect(Collectors.toList())
                )
                .stream()
                .map(messageFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteById(long id) {
        crudMessageRepository.deleteById(id);
    }

    @Override
    public void deleteAllById(List<Long> ids) {
        if (ids.isEmpty()) {
            return;
        }

        crudMessageRepository.deleteAllById(ids);
    }

    @Override
    @Transactional
    public void deleteAllByChatId(long chatId) {
        crudMessageRepository.deleteAllByChatId(chatId);
    }

    @Override
    public Message getById(long id) {
        return messageFactory.fromEntity(
                crudMessageRepository.findById(id)
                        .orElseThrow(ResourceNotFoundException::new)
        );
    }

    @Override
    public List<Message> getAllById(List<Long> ids) {
        return crudMessageRepository.findAllById(ids)
                .stream()
                .map(messageFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Message> getPageByChatIdAndOwningUserId(Page.Meta pageMeta, long chatId, long userId) {
        var pageable = PageRequest.of(
                pageMeta.getSequentialNumber(),
                pageMeta.getSize(),
                Sort.by("creationDate").descending()
        );
        var springPage = crudMessageRepository.findAllByChatIdAndOwningUserIdsContains(
                pageable,
                chatId,
                userId
        ).map(messageFactory::fromEntity);
        return new Page<>(
                new Page.Meta(
                        springPage.getPageable().getPageNumber(),
                        springPage.getPageable().getPageSize()
                ),
                new ArrayList<>(springPage.getContent())
        );
    }
}
