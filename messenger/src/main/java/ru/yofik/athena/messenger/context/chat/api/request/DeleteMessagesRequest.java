package ru.yofik.athena.messenger.context.chat.api.request;

import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
public class DeleteMessagesRequest {
    @NotNull
    @Size(min = 1)
    public List<Long> ids;
}
