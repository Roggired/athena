package ru.yofik.athena.messenger.api.http.chat.view;

import lombok.AllArgsConstructor;
import ru.yofik.athena.messenger.api.http.user.view.UserView;

import java.util.List;

@AllArgsConstructor
public class ChatView {
    public final long id;
    public final String name;
    public final List<UserView> userViews;
    public final MessageView lastMessage;
}
