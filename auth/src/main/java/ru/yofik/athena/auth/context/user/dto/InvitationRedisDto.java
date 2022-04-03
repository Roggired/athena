package ru.yofik.athena.auth.context.user.dto;

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
@RedisHash(value = "INVITATION", timeToLive = 24*60*60)
public class InvitationRedisDto {
    @Id
    @EqualsAndHashCode.Include
    private String code;
    private int count;
    @Indexed
    private long userId;
}
