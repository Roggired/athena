package ru.yofik.athena.messenger.domain.user.repository;

import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.messenger.domain.user.model.User;
import ru.yofik.athena.messenger.infrastructure.integration.auth.FilteredUsersRequest;

public interface UserRepository {
    NewPage<User> getPage(NewPage.Meta pageMeta, FilteredUsersRequest request);
    User getUser(long id);
    User updateUser(User user);
    User getCurrentUser();
}
