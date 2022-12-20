package ru.yofik.athena.messenger.api.http.user.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.user.view.UserView;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class UserViewMapper implements ConversionServiceConfig.Mapper<User, UserView> {
    @Override
    public UserView convert(User user) {
        return new UserView(
                user.getId(),
                user.getEmail(),
                user.getLogin(),
                user.isOnline(),
                user.getLastOnlineTime() != null ? user.getLastOnlineTime().toString() : null
        );
    }
}
