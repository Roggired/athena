package ru.yofik.athena.messenger.infrastructure.storage.redis.chat.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.yofik.athena.messenger.infrastructure.storage.redis.chat.entity.JoinChatInvitationEntity;

import java.util.List;

@Repository
public interface CrudJoinChatInvitationRepository extends CrudRepository<JoinChatInvitationEntity, String> {
    List<JoinChatInvitationEntity> findAllByRecipientId(long recipientId);
}
