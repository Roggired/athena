package ru.yofik.athena.messenger.api.http.chat.request;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class InviteUserToGroupChatRequest {
    public long chatId;
    public long userId;
}
