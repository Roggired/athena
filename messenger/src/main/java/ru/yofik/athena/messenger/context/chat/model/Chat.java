package ru.yofik.athena.messenger.context.chat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.comparator.Comparators;
import ru.yofik.athena.messenger.context.chat.view.ChatFullView;
import ru.yofik.athena.messenger.context.chat.view.ChatView;
import ru.yofik.athena.messenger.context.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Chat {
    private final long id;
    private String name;
    private final List<User> users = new ArrayList<>();
    private final List<Message> messages = new ArrayList<>();


    public Chat(long id, String name, List<User> users, List<Message> messages) {
        this.id = id;
        this.name = name;
        this.users.addAll(users);
        this.messages.addAll(messages);
    }

    public Chat(String name) {
        this.id = 0;
        this.name = name;
    }
    
    
    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

    public void hideMessagesForUser(User user) {
        var messagesToDelete = messages.stream()
                .filter(message -> !message.getOwningUserIds().contains(user.getId()))
                .collect(Collectors.toList());
        messages.removeAll(messagesToDelete);
    }

    public ChatView toView() {
        return new ChatView(
                id,
                name,
                users.stream()
                        .map(User::toView)
                        .collect(Collectors.toList()),
                messages.stream()
                        .sorted((a, b) -> Comparators.comparable().compare(b.getCreationDate(), a.getCreationDate()))
                        .map(Message::toView)
                        .findFirst()
                        .orElse(null)
        );
    }

    public ChatFullView toFullView() {
        return new ChatFullView(
                id,
                name,
                users.stream()
                        .map(User::toView)
                        .collect(Collectors.toList()),
                messages.stream()
                        .sorted((a, b) -> Comparators.comparable().compare(a.getCreationDate(), b.getCreationDate()))
                        .map(Message::toView)
                        .collect(Collectors.toList())
        );
    }
}
