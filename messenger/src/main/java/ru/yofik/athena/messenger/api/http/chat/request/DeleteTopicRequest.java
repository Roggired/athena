package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
public class DeleteTopicRequest {
    @NotEmpty
    public List<Long> deletedTopicIds;
}
