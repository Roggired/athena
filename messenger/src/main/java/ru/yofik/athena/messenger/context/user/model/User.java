package ru.yofik.athena.messenger.context.user.model;

import lombok.*;
import ru.yofik.athena.messenger.context.user.view.UserView;

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


    public UserView toView() {
        return new UserView(
                id,
                name,
                login
        );
    }
}
