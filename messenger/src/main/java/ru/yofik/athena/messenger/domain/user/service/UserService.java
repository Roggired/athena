package ru.yofik.athena.messenger.domain.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.common.domain.NewPage;
import ru.yofik.athena.common.domain.Page;
import ru.yofik.athena.messenger.api.http.user.request.UpdateUserRequest;
import ru.yofik.athena.messenger.domain.user.model.User;

@Service
public interface UserService {
    NewPage<User> getPage(NewPage.Meta pageMeta);
    User getById(long id);
    User updateUser(UpdateUserRequest request);
    User getCurrentUser();
}
