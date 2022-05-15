package ru.yofik.athena.messenger.api.http.chat.mapper;

import org.springframework.stereotype.Component;
import ru.yofik.athena.messenger.api.http.chat.view.TopicView;
import ru.yofik.athena.messenger.domain.chat.model.Topic;
import ru.yofik.athena.messenger.infrastructure.config.ConversionServiceConfig;

@Component
public class TopicViewMapper implements ConversionServiceConfig.Mapper<Topic, TopicView> {
    @Override
    public TopicView convert(Topic topic) {
        return new TopicView(
                topic.getId(),
                topic.getName()
        );
    }
}
