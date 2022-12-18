package ru.yofik.athena.auth.api.rest.user.views;

import lombok.AllArgsConstructor;
import ru.yofik.athena.auth.domain.user.model.Role;
import ru.yofik.athena.auth.domain.user.model.User;

import java.time.LocalDateTime;

@AllArgsConstructor
public class UserShortView {
    public long id;
    public String email;
    public String login;
    public Role role;
    public LocalDateTime lastLoginDate;
    public boolean isLocked;


    public static UserShortView from(User user) {
        return new UserShortView(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.getRole(),
                user.getLastLoginDate(),
                user.isLocked()
        );
    }
}
