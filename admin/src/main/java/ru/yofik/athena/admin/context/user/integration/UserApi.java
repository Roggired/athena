package ru.yofik.athena.admin.context.user.integration;

import org.springframework.stereotype.Service;
import ru.yofik.athena.admin.context.user.model.Invitation;
import ru.yofik.athena.admin.context.user.model.User;
import ru.yofik.athena.admin.context.user.model.UserInfo;

import java.util.List;
import java.util.Optional;

@Service
public interface UserApi {
    Optional<User> findById(long id, char[] token);
    List<UserInfo> findAll(char[] token);
    void deleteUser(long id, char[] token);
    void lockUser(long id, char[] token);
    void unlockUser(long id, char[] token);
    User createUser(User user, char[] token);
    Invitation createNewInvitation(long id, char[] token);
}