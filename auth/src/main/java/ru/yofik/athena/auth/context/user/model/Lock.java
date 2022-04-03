package ru.yofik.athena.auth.context.user.model;

import lombok.*;
import ru.yofik.athena.auth.context.user.view.LockView;

import java.time.ZonedDateTime;

@AllArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Lock {
    @EqualsAndHashCode.Include
    private final long id;
    private final ZonedDateTime date;
    private final LockReason lockReason;


    public LockView toView() {
        return new LockView(
                date.toString(),
                lockReason.name()
        );
    }
}
