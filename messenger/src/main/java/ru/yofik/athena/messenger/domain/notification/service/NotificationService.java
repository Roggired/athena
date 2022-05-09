package ru.yofik.athena.messenger.domain.notification.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.domain.notification.model.Notification;

@Service
public interface NotificationService {
    void sendNotification(Notification notification);
}
