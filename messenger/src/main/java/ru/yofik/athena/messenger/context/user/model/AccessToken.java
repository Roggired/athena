package ru.yofik.athena.messenger.context.user.model;

import lombok.*;
import ru.yofik.athena.messenger.context.user.view.AccessTokenView;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AccessToken {
    private final char[] data;


    public AccessTokenView toView() {
        return new AccessTokenView(
                new String(data)
        );
    }
}
