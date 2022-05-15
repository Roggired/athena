package ru.yofik.athena.messenger.domain.notification.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.user.model.User;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChangedUserPayload {
    private final User user;
    private final long chatId;
}
