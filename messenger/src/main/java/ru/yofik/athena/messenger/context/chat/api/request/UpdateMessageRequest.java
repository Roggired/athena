package ru.yofik.athena.messenger.context.chat.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateMessageRequest {
    @NotBlank
    public String text;
}
