package ru.yofik.athena.messenger.api.http.user.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.user.view.MyUserView;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class MyUserViewMapper implements ConversionServiceConfig.Mapper<User, MyUserView> {
    @Override
    public MyUserView convert(User user) {
        return new MyUserView(
                user.getId(),
                user.getName(),
                user.getLogin()
        );
    }
}