package ru.yofik.athena.messenger.context.chat.view;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DeletedMessagesNotificationView {
    public String type;
    public List<Long> deletedMessages;
}
