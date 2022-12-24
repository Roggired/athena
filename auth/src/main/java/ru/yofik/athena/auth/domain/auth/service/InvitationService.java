package ru.yofik.athena.auth.domain.auth.service;

import ru.yofik.athena.auth.domain.user.model.User;

public interface InvitationService {
    String inviteUser(User user);
    String inviteUserWithoutEmailNotification(User user);
}
