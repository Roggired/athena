package ru.yofik.athena.auth.context.user.model;

import lombok.*;
import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.context.user.view.ClientUserView;
import ru.yofik.athena.auth.context.user.view.UserShortView;
import ru.yofik.athena.auth.context.user.view.UserView;

import java.time.ZonedDateTime;

@AllArgsConstructor
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
    @EqualsAndHashCode.Include
    @Getter
    private final long id;
    @Getter
    @Setter
    private String name;
    @Getter
    @Setter
    private String login;
    @Getter
    private Lock lock;
    @Getter
    @Setter
    private String allowedDeviceId;
    @Getter
    private final ZonedDateTime createdAt;
    @Getter
    @Setter
    private Invitation invitation;
    @Getter
    @Setter
    private boolean activated;


    public User(long id,
                String name,
                String login,
                String allowedDeviceId,
                ZonedDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.allowedDeviceId = allowedDeviceId;
        this.createdAt = createdAt;
        this.activated = false;
    }


    public boolean isLocked() {
        return lock != null;
    }

    public boolean hasInvitation() {
        return invitation != null;
    }

    public void lock(Lock lock) {
        if (lock == null) {
            throw new IllegalArgumentException();
        }

        this.lock = lock;
    }

    public void unlock() {
        this.lock = null;
    }

    public UserView toView() {
        return new UserView(
                id,
                name,
                login,
                allowedDeviceId,
                createdAt.toString(),
                isLocked() ? lock.toView() : null,
                hasInvitation() ? invitation.toView() : null,
                activated
        );
    }

    public ClientUserView toClientView() {
        return new ClientUserView(
                id,
                name,
                login
        );
    }

    public UserShortView toShortView() {
        return new UserShortView(
                id,
                name,
                login,
                isLocked(),
                createdAt.toString(),
                activated
        );
    }
}
