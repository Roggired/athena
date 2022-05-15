package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.user.mapper.UserViewMapper;
import ru.yofik.athena.messenger.api.ws.notification.view.ChangedUserPayloadView;
import ru.yofik.athena.messenger.domain.notification.model.ChangedUserPayload;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class ChangedUserPayloadViewMapper implements ConversionServiceConfig.Mapper<ChangedUserPayload, ChangedUserPayloadView> {
    @Override
    public ChangedUserPayloadView convert(ChangedUserPayload payload) {
        var userViewMapper = new UserViewMapper();
        return new ChangedUserPayloadView(
                userViewMapper.convert(payload.getUser()),
                payload.getChatId()
        );
    }
}
