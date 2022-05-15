package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.TopicEntity;

import java.util.List;

@Repository
public interface CrudTopicRepository extends JpaRepository<TopicEntity, Long> {
    List<TopicEntity> findAllByChatId(long chatId);
}
