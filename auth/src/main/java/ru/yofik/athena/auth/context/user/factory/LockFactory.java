package ru.yofik.athena.auth.context.user.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.auth.context.user.dto.LockJpaDto;
import ru.yofik.athena.auth.context.user.model.Lock;
import ru.yofik.athena.auth.context.user.model.LockReason;

import java.time.ZonedDateTime;

@Component
public class LockFactory {
    public Lock create(LockReason lockReason) {
        return new Lock(
                0,
                ZonedDateTime.now(),
                lockReason
        );
    }

    public Lock from(LockJpaDto lockJpaDto) {
        if (lockJpaDto == null) {
            return null;
        }

        return new Lock(
                lockJpaDto.getId(),
                lockJpaDto.getDate(),
                lockJpaDto.getReason()
        );
    }

    public LockJpaDto toJpaDto(Lock lock) {
        if (lock == null) {
            return null;
        }

        return new LockJpaDto(
                lock.getId(),
                lock.getDate(),
                lock.getLockReason()
        );
    }
}
