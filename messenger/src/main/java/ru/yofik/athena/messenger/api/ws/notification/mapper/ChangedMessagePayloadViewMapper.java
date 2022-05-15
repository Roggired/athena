package ru.yofik.athena.messenger.api.ws.notification.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.mapper.MessageViewMapper;
import ru.yofik.athena.messenger.api.ws.notification.view.ChangedMessagePayloadView;
import ru.yofik.athena.messenger.domain.notification.model.ChangedMessagePayload;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class ChangedMessagePayloadViewMapper implements ConversionServiceConfig.Mapper<ChangedMessagePayload, ChangedMessagePayloadView> {
    @Override
    public ChangedMessagePayloadView convert(ChangedMessagePayload payload) {
        var messageViewMapper = new MessageViewMapper();
        return new ChangedMessagePayloadView(
                payload.getUserId(),
                messageViewMapper.convert(payload.getMessage())
        );
    }
}
