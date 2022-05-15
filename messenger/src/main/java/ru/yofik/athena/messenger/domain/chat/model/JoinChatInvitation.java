package ru.yofik.athena.messenger.domain.chat.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.user.model.User;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class JoinChatInvitation {
    private final String id;
    private final long recipientId;
    private final User sender;
    private final Chat chat;
}
