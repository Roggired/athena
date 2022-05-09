package ru.yofik.athena.messenger.api.http.user.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.user.view.AccessTokenView;
import ru.yofik.athena.messenger.domain.user.model.AccessToken;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class AccessTokenViewMapper implements ConversionServiceConfig.Mapper<AccessToken, AccessTokenView> {
    @Override
    public AccessTokenView convert(AccessToken accessToken) {
        return new AccessTokenView(
                new String(accessToken.getData())
        );
    }
}
