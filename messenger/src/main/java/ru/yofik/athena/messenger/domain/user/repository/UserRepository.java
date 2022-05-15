package ru.yofik.athena.messenger.domain.user.repository;

import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.domain.user.model.User;

import java.util.List;

public interface UserRepository {
    Page<User> getPage(Page.Meta pageMeta);
    User getUser(long id);
    User updateUser(User user);
    User getCurrentUser();
}
