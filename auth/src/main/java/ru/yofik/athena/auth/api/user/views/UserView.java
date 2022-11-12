package ru.yofik.athena.auth.api.user.views;

import lombok.AllArgsConstructor;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UserView {
    public long id;
    public String login;
    public Role role;
    public boolean isLocked;
    public String lockReason;
    public LocalDateTime credentialsExpirationDate;
    public String allowedDeviceId;
    public LocalDateTime lastLoginDate;


    public static UserView from(User user) {
        return new UserView(
                user.getId(),
                user.getLogin(),
                user.getRole(),
                user.isLocked(),
                user.getLockReason(),
                user.getCredentialsExpirationDate(),
                user.getAllowedDeviceId(),
                user.getLastLoginDate()
        );
    }
}