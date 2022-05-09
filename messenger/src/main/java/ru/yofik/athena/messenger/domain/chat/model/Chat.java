package ru.yofik.athena.messenger.domain.chat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.comparator.Comparators;
import ru.yofik.athena.messenger.domain.user.model.User;

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
    private List<Message> messages = new ArrayList<>();


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

    public Chat hideMessagesForUser(User user) {
        messages = messages.stream()
                .filter(message -> message.getOwningUserIds().contains(user.getId()))
                .collect(Collectors.toList());
        return this;
    }

    public Chat sortMessages() {
        messages = messages.stream()
                .sorted((a, b) -> Comparators.comparable().compare(a.getCreationDate(), b.getCreationDate()))
                .collect(Collectors.toList());
        return this;
    }

    public Chat onlyLastMessage() {
        var lastMessage = messages.stream()
                .min((a, b) -> Comparators.comparable().compare(b.getCreationDate(), a.getCreationDate()))
                .orElse(null);
        messages.clear();
        if (lastMessage != null) {
            messages.add(lastMessage);
        }
        return this;
    }
}
