package ru.yofik.athena.messenger.context.chat.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class SendMessageRequest {
    public Long chatId;
    @NotBlank
    public String text;
}
