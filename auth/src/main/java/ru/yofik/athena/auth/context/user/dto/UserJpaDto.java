package ru.yofik.athena.auth.context.user.dto;

import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "users")
public class UserJpaDto {
    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private long id;

    @Column
    private String name;

    @Column
    private String login;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "lock_id")
    private LockJpaDto lock;

    @Column
    private String allowedDeviceId;

    @Column
    private ZonedDateTime createdAt;

    @Column
    private boolean activated;
}
