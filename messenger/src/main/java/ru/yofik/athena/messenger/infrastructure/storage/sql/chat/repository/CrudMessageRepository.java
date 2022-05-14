package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.MessageEntity;

@Repository
public interface CrudMessageRepository extends JpaRepository<MessageEntity, Long> {
    Page<MessageEntity> findAllByChatIdAndOwningUserIdsContains(Pageable pageable, long chatId, long userId);
    void deleteAllByChatId(long chatId);
}
