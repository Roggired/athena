package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.ChatEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface CrudChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query("select c from ChatEntity c where :userAId MEMBER OF c.userIds AND :userBId MEMBER OF c.userIds")
    Optional<ChatEntity> getByUserIds(Long userAId, Long userBId);
}
