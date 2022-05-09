package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public abstract class Notification {
    protected final NotificationType type;
    protected final List<Long> targetUserIds;
}
