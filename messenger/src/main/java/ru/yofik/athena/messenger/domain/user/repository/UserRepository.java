package ru.yofik.athena.messenger.domain.user.repository;

import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

public interface UserRepository {
    List<User> getAllUsers();
    User getUser(long id);
    User updateUser(User user);
    User getCurrentUser();
}
