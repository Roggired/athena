package ru.yofik.athena.messenger.context.chat.dto;

import lombok.*;

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
public class ChatJpaDto {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @ElementCollection(targetClass = Long.class)
    @CollectionTable(
            name = "chat_user",
            joinColumns = @JoinColumn(name = "chat_id")
    )
    @Column(name = "user_id")
    private List<Long> userIds;

    @OneToMany(
            mappedBy = "chat",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL
    )
    private List<MessageJpaDto> messages;
}
