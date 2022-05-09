package ru.yofik.athena.messenger.domain.user.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    private final long id;
    private String name;
    private String login;
}
