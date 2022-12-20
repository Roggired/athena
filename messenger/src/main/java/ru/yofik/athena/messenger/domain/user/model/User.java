package ru.yofik.athena.messenger.domain.user.model;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private final long id;
    private String email;
    private String login;
    private boolean online;
    private LocalDateTime lastOnlineTime; // in UTC
}
