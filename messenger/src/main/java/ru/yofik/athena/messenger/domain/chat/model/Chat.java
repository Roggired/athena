package ru.yofik.athena.messenger.domain.chat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.yofik.athena.messenger.domain.notification.service.NotificationService;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {
    private final long id;
    private String name;
    private final ChatType type;
    private final List<User> users = new ArrayList<>();
    private Message lastMessage;

    public Chat(long id, String name, ChatType type, List<User> users) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.users.addAll(users);
        this.lastMessage = null;
    }

    private Chat(String name, ChatType type) {
        this.id = 0;
        this.name = name;
        this.type = type;
        this.lastMessage = null;
    }

    public static Chat newPersonalChat(User userA, User userB) {
        var chat = new Chat(
                "personal-chat-for-users-" + userA.getId() + "-and-" + userB.getId(),
                ChatType.PERSONAL
        );
        chat.addUser(userA);
        chat.addUser(userB);
        return chat;
    }

    public static Chat newGroupChat(String name, User initiator) {
        var chat = new Chat(name, ChatType.GROUP);
        chat.addUser(initiator);
        return chat;
    }
    
    
    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public Chat chooseChatNameFor(User userFor) {
        if (type == ChatType.GROUP) {
            return this;
        }

        var otherUser = users
                .stream()
                .filter(user -> !user.equals(userFor))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        this.name = otherUser.getName();
        return this;
    }

    public Chat markOnlineUsers(NotificationService notificationService) {
        users.forEach(user -> user.setOnline(notificationService.isUserActive(user.getId())));
        return this;
    }
}
