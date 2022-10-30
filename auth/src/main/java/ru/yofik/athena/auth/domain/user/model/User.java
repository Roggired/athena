package ru.yofik.athena.auth.domain.user.model;

import lombok.*;
import ru.yofik.athena.auth.utils.TimeUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Base64;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column
    private String login;
    @Column
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
            String login,
            String invitation,
            String allowedDeviceId
    ) {
        return new User(
                0,
                login,
                Role.USER,
                new Lock(0, false, ""),
                Credentials.newUserCredentials(invitation),
                Session.newSession(allowedDeviceId)
        );
    }

    public static User newAdmin(
            String login,
            String password,
            String allowedDeviceId
    ) {
        return new User(
                0,
                login,
                Role.ADMIN,
                new Lock(0, false, ""),
                Credentials.newAdminCredentials(password),
                Session.newSession(allowedDeviceId)
        );
    }


    public boolean isLocked() {
        return lock.isLocked();
    }

    public String getLockReason() {
        return lock.getReason();
    }


    public boolean isCredentialsExpired() {
        var currentTime = TimeUtils.now();
        return credentials.isExpired(currentTime);
    }

    public boolean challengeCredentials(String userCredentials, String allowedDeviceId) {
        if (!session.getAllowedDeviceId().equals(allowedDeviceId)) return false;
        return credentials.challenge(userCredentials);
    }

    public LocalDateTime getCredentialsExpirationDate() {
        return credentials.getExpirationDate();
    }

    public String hashedCredentials() {
        return credentials.getValue();
    }


    public boolean isAllowed(String deviceId) {
        return deviceId.equals(session.getAllowedDeviceId());
    }

    public String getAllowedDeviceId() {
        return session.getAllowedDeviceId();
    }

    public LocalDateTime getLastLoginDate() {
        return session.getLastLoginDate();
    }
}
