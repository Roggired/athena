package ru.yofik.athena.messenger.context.chat.view;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class MessageView {
    public final long id;
    public final String text;
    public final long senderId;
    public final long chatId;
    public final String creationDate;
    public final String modificationDate;
}
