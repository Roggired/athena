package ru.yofik.athena.messenger.api.http.chat.view;

import lombok.AllArgsConstructor;
import ru.yofik.athena.messenger.api.http.user.view.UserView;

@AllArgsConstructor
public class JoinChatInvitationView {
    public String id;
    public UserView sender;
    public ChatView chat;
}
