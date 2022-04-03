package ru.yofik.athena.auth.context.user.dto;

import lombok.*;
import ru.yofik.athena.auth.context.user.model.LockReason;

import javax.persistence.*;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_lock")
public class LockJpaDto {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;

    @Column
    private ZonedDateTime date;

    @Column
    @Enumerated(EnumType.STRING)
    private LockReason reason;
}
