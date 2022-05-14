package ru.yofik.athena.messenger.domain.chat.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.util.comparator.Comparators;
import ru.yofik.athena.common.Page;
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
    private Message lastMessage;

    public Chat(long id, String name, List<User> users) {
        this.id = id;
        this.name = name;
        this.users.addAll(users);
        this.lastMessage = null;
    }

    public Chat(String name) {
        this.id = 0;
        this.name = name;
        this.lastMessage = null;
    }
    
    
    public void addUser(User user) {
        users.add(user);
    }

    public void removeUser(User user) {
        users.remove(user);
    }

//    public Chat hideMessagesForUser(User user) {
//        messages = messages.stream()
//                .filter(message -> message.getOwningUserIds().contains(user.getId()))
//                .collect(Collectors.toList());
//        return this;
//    }

//    public Chat sortMessages() {
//        messages = messages.stream()
//                .sorted((a, b) -> Comparators.comparable().compare(a.getCreationDate(), b.getCreationDate()))
//                .collect(Collectors.toList());
//        return this;
//    }

//    public Chat onlyLastMessage() {
//        var lastMessage = messages.stream()
//                .min((a, b) -> Comparators.comparable().compare(b.getCreationDate(), a.getCreationDate()))
//                .orElse(null);
//        messages.clear();
//        if (lastMessage != null) {
//            messages.add(lastMessage);
//        }
//        return this;
//    }

    public Chat chooseChatNameFor(User userFor) {
        var otherUser = users
                .stream()
                .filter(user -> !user.equals(userFor))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        this.name = otherUser.getName();
        return this;
    }
}
