package ru.yofik.athena.messenger.context.chat.dto;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "message")
public class MessageJpaDto {
    @Id
    @GeneratedValue
    private long id;

    @Column(name = "message_text")
    private String text;
    @Column
    private long senderId;
    @ManyToOne(optional = false)
    private ChatJpaDto chat;
    // UTC
    @Column
    private LocalDateTime date;
}
