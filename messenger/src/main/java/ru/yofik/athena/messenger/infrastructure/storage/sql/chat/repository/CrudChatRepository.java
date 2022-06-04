package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity.ChatEntity;

import java.util.Optional;

@Repository
public interface CrudChatRepository extends JpaRepository<ChatEntity, Long> {
    @Query("select c from ChatEntity c " +
            "where :userAId MEMBER OF c.userIds " +
            "AND :userBId MEMBER OF c.userIds " +
            "AND c.type = 'PERSONAL'")
    Optional<ChatEntity> getPersonalChatByUserIds(Long userAId, Long userBId);

    @Query("select c from ChatEntity c where :userId MEMBER OF c.userIds")
    Page<ChatEntity> findAllByUserId(Pageable pageable, long userId);

}
