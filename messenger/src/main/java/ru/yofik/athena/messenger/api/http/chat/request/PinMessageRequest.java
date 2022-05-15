package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class PinMessageRequest {
    public long messageId;
    public Long topicId;
}
