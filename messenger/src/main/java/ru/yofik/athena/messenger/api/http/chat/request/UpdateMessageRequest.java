package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@NoArgsConstructor
public class UpdateMessageRequest {
    @NotBlank
    public String text;
}
