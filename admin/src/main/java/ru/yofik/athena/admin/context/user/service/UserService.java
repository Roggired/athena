package ru.yofik.athena.admin.context.user.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.yofik.athena.admin.context.user.model.Invitation;
import ru.yofik.athena.admin.context.user.model.User;
import ru.yofik.athena.admin.context.user.model.UserInfo;

import java.io.InputStream;
import java.util.List;

@Service
public interface UserService {
    void createUser(String name, String login);
    String importUsers(InputStream inputStream);
    void deleteUser(long id);
    void lockUser(long id);
    void unlockUser(long id);
    User getUser(long id);
    List<UserInfo> getAllUsers();
    Invitation createNewInvitation(long id);
}
