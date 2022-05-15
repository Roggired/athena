package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class SendMessageRequest {
    public Long chatId;
    @NotBlank
    public String text;
    public Long topicId;
}
