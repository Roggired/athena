package ru.yofik.athena.messenger.context.chat.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
public class CreateChatRequest {
    @NotBlank
    public String name;
    @NotEmpty
    public List<Long> users;
}
