package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
public class ViewMessagesRequest {
    public List<Long> messageIds;
}
