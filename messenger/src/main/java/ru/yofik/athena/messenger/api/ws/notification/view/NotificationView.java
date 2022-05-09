package ru.yofik.athena.messenger.api.ws.notification.view;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationView<T> {
    public String type;
    public T payload;
}
