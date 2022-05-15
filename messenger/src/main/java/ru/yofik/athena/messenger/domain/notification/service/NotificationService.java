package ru.yofik.athena.messenger.domain.notification.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.domain.notification.model.Notification;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Service
public interface NotificationService {
    void sendNotification(Notification notification);
    boolean isUserActive(long userId);
}
