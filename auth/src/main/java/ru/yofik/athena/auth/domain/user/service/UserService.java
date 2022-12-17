package ru.yofik.athena.auth.domain.user.service;

import ru.yofik.athena.auth.api.user.requests.CreateUserRequest;
import ru.yofik.athena.auth.api.user.requests.FilteredUsersRequest;
import ru.yofik.athena.auth.api.user.requests.UpdateUserRequest;
import ru.yofik.athena.auth.domain.user.model.User;
import ru.yofik.athena.common.domain.NewPage;

public interface UserService {
    User createUser(CreateUserRequest request);
    User updateUser(long id, UpdateUserRequest request);
    User updateUser(User user);
    void deleteUser(long id);
    User getUser(long id);
    User getUser(String login);
    NewPage<User> getUsersPageable(NewPage.Meta pageMeta, FilteredUsersRequest request);
}