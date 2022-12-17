package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.auth.utils.TimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "athena_users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String email;
    private String login;
    @Enumerated(EnumType.STRING)
    private Role role;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lock_id")
    private Lock lock;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "credentials_id")
    private Credentials credentials;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "session_id")
    private Session session;


    public static User newUser(
            String email,
            String login,
            String invitation
    ) {
        return new User(
                0,
                email,
                login,
                Role.USER,
                new Lock(0, false, ""),
                Credentials.newUserCredentials(invitation),
                Session.newSession()
        );
    }

    public static User newAdmin(
            String email,
            String login,
            String password,
            boolean isPasswordTemporary
    ) {
        return new User(
                0,
                email,
                login,
                Role.ADMIN,
                new Lock(0, false, ""),
                Credentials.newAdminCredentials(password, isPasswordTemporary),
                Session.newSession()
        );
    }


    public boolean isLocked() {
        return lock.isLocked();
    }

    public String getLockReason() {
        return lock.getReason();
    }


    public boolean isCredentialsExpired() {
        var currentTime = TimeUtils.nowUTC();
        return credentials.isExpired(currentTime);
    }

    public boolean challengeCredentials(String userCredentials) {
        return credentials.challenge(userCredentials);
    }

    public LocalDateTime getCredentialsExpirationDate() {
        return credentials.getExpirationDate();
    }

    public LocalDateTime getLastLoginDate() {
        if (session == null) return TimeUtils.infinity();
        return session.getLastLoginDate();
    }

    public void lock(String reason) {
        lock.setLocked(true);
        lock.setReason(reason);
    }

    public void unlock() {
        lock.setLocked(false);
        lock.setReason("");
    }
}
