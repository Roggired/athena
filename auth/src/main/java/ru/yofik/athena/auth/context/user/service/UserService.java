package ru.yofik.athena.auth.context.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.context.user.api.request.*;
import ru.yofik.athena.auth.context.user.model.LockReason;
import ru.yofik.athena.auth.context.user.view.*;
import ru.yofik.athena.common.Page;

import java.util.List;

@Service
public interface UserService {
    AccessTokenView activate(ActivateUserRequest request, String deviceId);
    UserView create(CreateUserRequest request);
    UserView get(long id);
    List<UserShortView> getAll();
    Page<UserShortView> getPage(Page.Meta pageMeta);
    void lock(long id, LockReason lockReason);
    void unlock(long id);
    InvitationView createInvitation(CreateInvitationRequest request);
    void delete(long id);
    ClientUserView authorize(AuthorizeUserRequest request, String deviceId);
    UserView updateUser(long id, UpdateUserRequest request);
}
