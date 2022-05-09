package ru.yofik.athena.messenger.domain.user.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccessToken {
    private final char[] data;
}
