package ru.yofik.athena.messenger.infrastructure.storage.redis.chat.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RedisHash(value = "JOIN_CHAT_INVITATION", timeToLive = 24*60*60)
public class JoinChatInvitationEntity {
    @Id
    @EqualsAndHashCode.Include
    private String id;
    @Indexed
    private long recipientId;
    private long senderId;
    private long chatId;
}
