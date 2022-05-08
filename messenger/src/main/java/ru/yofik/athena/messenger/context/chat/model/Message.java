package ru.yofik.athena.messenger.context.chat.model;

import lombok.*;
import ru.yofik.athena.messenger.context.chat.view.MessageView;

import java.time.LocalDateTime;
import java.util.List;

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
    // UTC
    private final LocalDateTime creationDate;
    // UTC
    private final LocalDateTime modificationDate;

    private final List<Long> owningUserIds;


    public MessageView toView() {
        return new MessageView(
                id,
                text,
                senderId,
                chatId,
                creationDate.toString(),
                modificationDate.toString()
        );
    }
}
