package ru.yofik.athena.messenger.infrastructure.storage.sql.chat;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.api.exception.ResourceNotFoundException;
import ru.yofik.athena.messenger.domain.chat.model.Chat;
import ru.yofik.athena.messenger.domain.chat.repository.ChatRepository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.factory.ChatFactory;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository.CrudChatRepository;
import ru.yofik.athena.messenger.utils.PageUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ChatRepositoryImpl implements ChatRepository {
    private final CrudChatRepository crudChatRepository;
    private final ChatFactory chatFactory;

    public ChatRepositoryImpl(CrudChatRepository crudChatRepository, ChatFactory chatFactory) {
        this.crudChatRepository = crudChatRepository;
        this.chatFactory = chatFactory;
    }

    @Override
    public Chat getById(long id) {
        var chatEntity = crudChatRepository.findById(id)
                .orElseThrow(ResourceNotFoundException::new);
        return chatFactory.fromEntity(chatEntity);
    }

    @Override
    public Chat save(Chat chat) {
        var chatEntity = chatFactory.toEntity(chat);
        return chatFactory.fromEntity(crudChatRepository.save(chatEntity));
    }

    @Override
    public void delete(long id) {
        crudChatRepository.deleteById(id);
    }

    @Override
    public List<Chat> getAll() {
        return crudChatRepository.findAll()
                .stream()
                .map(chatFactory::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Chat> getPage(Page.Meta pageMeta) {
        var springPage = crudChatRepository.findAll(
                PageRequest.of(
                        pageMeta.getSequentialNumber(),
                        pageMeta.getSize()
                )
        ).map(chatFactory::fromEntity);
        return PageUtils.fromSpringPage(springPage);
    }

    @Override
    public Page<Chat> getPageByUserId(Page.Meta pageMeta, long userId) {
        var springPage = crudChatRepository.findAllByUserId(
                PageRequest.of(
                        pageMeta.getSequentialNumber(),
                        pageMeta.getSize()
                ),
                userId
        ).map(chatFactory::fromEntity);
        return PageUtils.fromSpringPage(springPage);
    }

    @Override
    public boolean existsById(long id) {
        return crudChatRepository.existsById(id);
    }

    @Override
    public Optional<Chat> getByUserIds(long userAId, long userBId) {
        return crudChatRepository.getPersonalChatByUserIds(userAId, userBId).map(chatFactory::fromEntity);
    }
}
