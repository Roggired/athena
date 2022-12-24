package ru.yofik.athena.auth.domain.user.service;

import ru.yofik.athena.auth.api.rest.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.rest.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.rest.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.common.domain.NewPage;

import java.util.List;

public interface UserService {
    User createUser(CreateUserRequest request);
    User updateUser(long id, UpdateUserRequest request);
    User updateUser(User user);
    void deleteUser(long id);
    User getUserById(long id);
    User getUserByLogin(String login);
    User getUserByEmail(String email);
    List<User> getAdmins();
    NewPage<User> getUsersPageable(NewPage.Meta pageMeta, FilteredUsersRequest request);
}