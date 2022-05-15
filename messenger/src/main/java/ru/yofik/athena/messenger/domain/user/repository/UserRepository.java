package ru.yofik.athena.messenger.domain.user.repository;

import ru.yofik.athena.common.Page;
import ru.yofik.athena.messenger.domain.user.model.User;

public interface UserRepository {
    Page<User> getPage(Page.Meta pageMeta);
    User getUser(long id);
    User updateUser(User user);
    User getCurrentUser();
}
