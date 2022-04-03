package ru.yofik.athena.messenger.context.chat.view;

import lombok.AllArgsConstructor;
import ru.yofik.athena.messenger.context.user.view.UserView;

import java.util.List;

@AllArgsConstructor
public class ChatFullView {
    public final long id;
    public final String name;
    public final List<UserView> users;
    public final List<MessageView> messages;
}
