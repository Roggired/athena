package ru.yofik.athena.admin.context.user.factory;

import org.springframework.stereotype.Component;
import ru.yofik.athena.admin.context.user.model.User;

@Component
public class UserFactory {
    public User of(String name, String login) {
        return new User(
                0L,
                name,
                login,
                "",
                "",
                null,
                null,
                false
        );
    }
}
