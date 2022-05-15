package ru.yofik.athena.messenger.infrastructure.storage.sql.chat.entity;

import lombok.*;
import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "message_topic")
public class TopicEntity {
    @Id
    @GeneratedValue
    private long id;

    @Column
    private String name;

    @Column
    private long chatId;
}
