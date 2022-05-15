package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity;

import lombok.*;
import ru.yofik.athena.messenger.domain.chat.model.ChatType;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "chat")
public class ChatEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Enumerated(EnumType.STRING)
    private ChatType type;

    @ElementCollection(targetClass = Long.class)
    @CollectionTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id")
    )
    @Column(name = "user_id")
    private List<Long> userIds;
}
