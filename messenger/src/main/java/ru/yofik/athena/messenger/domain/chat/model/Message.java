package ru.yofik.athena.messenger.domain.chat.model;

import lombok.*;
import ru.yofik.athena.common.utils.TimeUtils;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Message {
    private final long id;
    private String text;
    private final long senderId;
    private final long chatId;
    private final LocalDateTime creationDate; // UTC
    private LocalDateTime modificationDate; // UTC
    private final List<Long> owningUserIds;
    private final List<Long> viewedByUserIds;
    private Topic topic;
    private boolean pinned;

    public static Message newMessage(String text, User sender, Chat chat) {
        var creationDate = TimeUtils.nowUTC();
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
                        .collect(Collectors.toList()),
                List.of(sender.getId()),
                Topic.DEFAULT_TOPIC,
                false
        );
    }


    public Message update(String newText) {
        this.text = newText;
        this.modificationDate = TimeUtils.nowUTC();
        return this;
    }

    public Message pin() {
        this.pinned = true;
        return this;
    }

    public Message pin(Topic topic) {
        this.pinned = true;
        this.topic = topic;
        return this;
    }

    public Message unpin() {
        this.pinned = false;
        return this;
    }

    public Message setTopic(Topic topic) {
        this.topic = topic;
        return this;
    }
}
