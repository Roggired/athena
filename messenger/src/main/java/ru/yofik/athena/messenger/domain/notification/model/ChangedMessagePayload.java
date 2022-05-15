package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.chat.model.Message;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChangedMessagePayload {
    private final long userId;
    private final Message message;
}
