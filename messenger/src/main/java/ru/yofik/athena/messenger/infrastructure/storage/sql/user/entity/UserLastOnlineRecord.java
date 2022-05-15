package ru.yofik.athena.messenger.infrastructure.storage.sql.user.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "user_last_online_record")
public class UserLastOnlineRecord {
    @Id
    private long userId;

    @Column(name = "last_online")
    private LocalDateTime lastOnlineTime; // in UTC
}
