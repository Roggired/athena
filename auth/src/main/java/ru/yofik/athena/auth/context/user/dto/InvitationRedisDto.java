package ru.yofik.athena.auth.context.user.dto;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@RedisHash(value = "INVITATION", timeToLive = 24*60*60)
public class InvitationRedisDto {
    @EqualsAndHashCode.Include
    private String code;
    private int count;
    private long userId;
}
