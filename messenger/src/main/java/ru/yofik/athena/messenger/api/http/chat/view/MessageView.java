package ru.yofik.athena.messenger.api.http.chat.view;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class MessageView {
    public final long id;
    public final String text;
    public final long senderId;
    public final long chatId;
    public final String creationDate;
    public final String modificationDate;
    public final List<Long> viewedByUserIds;
    public final TopicView topic;
    public final boolean isPinned;
}
