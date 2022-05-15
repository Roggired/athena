package ru.yofik.athena.messenger.domain.chat.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Topic {
    public static final Topic DEFAULT_TOPIC = new Topic(-1, "NO_TOPIC", -1);


    @EqualsAndHashCode.Include
    private final long id;
    private String name;
    private final long chatId;


    public static Topic newTopic(String name, long chatId) {
        return new Topic(
                0,
                name,
                chatId
        );
    }
}
