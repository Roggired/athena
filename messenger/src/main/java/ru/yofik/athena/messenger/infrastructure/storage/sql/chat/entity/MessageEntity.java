package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "message")
public class MessageEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "message_text")
    private String text;

    @Column
    private long senderId;

    @ManyToOne(optional = false)
    private ChatEntity chat;

    // UTC
    @Column
    private LocalDateTime creationDate;

    // UTC
    @Column
    private LocalDateTime modificationDate;

    @ElementCollection(targetClass = Long.class)
    @CollectionTable(
            name = "message_user",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "user_id")
    private List<Long> owningUserIds;

    @ElementCollection(targetClass = Long.class)
    @CollectionTable(
            name = "message_views",
            joinColumns = @JoinColumn(name = "message_id")
    )
    @Column(name = "user_id")
    private List<Long> viewedByUserIds;
}
