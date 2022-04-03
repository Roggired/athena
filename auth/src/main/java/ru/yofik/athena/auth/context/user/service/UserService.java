package ru.yofik.athena.auth.context.user.service;

import org.springframework.stereotype.Service;
import ru.yofik.athena.auth.context.user.api.request.ActivateUserRequest;
import ru.yofik.athena.auth.context.user.api.request.AuthorizeUserRequest;
import ru.yofik.athena.auth.context.user.api.request.CreateInvitationRequest;
import ru.yofik.athena.auth.context.user.api.request.CreateUserRequest;
import ru.yofik.athena.auth.context.user.model.LockReason;
import ru.yofik.athena.auth.context.user.view.ClientUserView;
import ru.yofik.athena.auth.context.user.view.InvitationView;
import ru.yofik.athena.auth.context.user.view.UserShortView;
import ru.yofik.athena.auth.context.user.view.UserView;

import java.util.List;

@Service
public interface UserService {
    String activate(ActivateUserRequest request);
    void create(CreateUserRequest request);
    UserView get(long id);
    List<UserShortView> getAll();
    void lock(long id, LockReason lockReason);
    InvitationView createInvitation(CreateInvitationRequest request);
    void delete(long id);
    ClientUserView authorize(AuthorizeUserRequest request, String deviceId);
}
