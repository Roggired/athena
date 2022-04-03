package ru.yofik.athena.auth.context.user.model;

import lombok.*;
import ru.yofik.athena.auth.context.user.view.InvitationView;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Invitation {
    @EqualsAndHashCode.Include
    private final String code;
    private int count;
    private final long userId;


    public void touch() {
        count--;
    }

    public InvitationView toView() {
        return new InvitationView(
                code,
                count
        );
    }
}
