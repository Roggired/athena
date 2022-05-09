package ru.yofik.athena.messenger.domain.chat.model;

import lombok.*;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {
    private final long id;
    private final String text;
    private final long senderId;
    private final long chatId;
    private final LocalDateTime creationDate; // UTC
    private final LocalDateTime modificationDate; // UTC
    private final List<Long> owningUserIds;

    public static Message newMessage(String text, User sender, Chat chat) {
        var creationDate = Instant.now().atZone(ZoneId.of("UTC")).toLocalDateTime();
        return new Message(
                0,
                text,
                sender.getId(),
                chat.getId(),
                creationDate,
                creationDate,
                chat.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toList())
        );
    }
}
