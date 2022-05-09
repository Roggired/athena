package ru.yofik.athena.messenger.domain.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

@Service
public interface UserService {
    List<User> getAllUsers();
    User getUser(long id);
    User updateUser(UpdateUserRequest request);
    User getCurrentUser();
}
